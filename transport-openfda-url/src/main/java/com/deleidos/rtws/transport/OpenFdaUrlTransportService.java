package com.deleidos.rtws.transport;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import com.deleidos.rtws.core.framework.Description;
import com.deleidos.rtws.core.framework.UserConfigured;

/**
 * Retrieves data from the openFDA API via URL Rest calls to transport into DE for processing.  
 * This transport is currently configured to retrieve recall/enforcement data from the 
 * https://api.fda.gov/drug/enforcement.json endpoint for the years 2004-2015.  Both the API 
 * endpoint and year range are configurable.  Records without the harmonized "openfda" fields 
 * are filtered out, but can be included via configuration change.
 */
@Description("Retrieves data from the openFDA API via URL Rest calls")
public class OpenFdaUrlTransportService extends AbstractTransportService {

	private Logger logger = Logger.getLogger(OpenFdaUrlTransportService.class);

	private ObjectMapper mapper = new ObjectMapper();
	private String baseURL = "https://api.fda.gov/drug/enforcement.json";
	private int startYear = 2004;
	private int endYear = 2015;
	private boolean nonOpenFDA = false;
	
	private String dateRange = "?search=report_date:[%d0101+TO+%d1231]";
	private int batchLimit = 100;

	public OpenFdaUrlTransportService() {
		super();
	}

	/**
	 * One time initialization step
	 */
	@Override
	public void initialize() {
		super.initialize();
		dateRange = formatDateRange();
		baseURL = baseURL + dateRange;
	}

	/**
	 * Clean up any resources on close
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * The public method invoked by the DigitalEdge pipeline to start the transport process of openFDA data
	 * into DigitalEdge.  Calls the openFDA REST API to retrieve enforcement/recall data and sends it into
	 * DigitalEdge for processing.
	 */
	@Override
	public void execute() {
		int count = 0, skip = 0;
		int total = getTotal();
		
		while (count < total) {
			JsonNode jsonNode = getData(baseURL, batchLimit, skip);
			if (jsonNode != null) {
				ArrayNode results = (ArrayNode)jsonNode.get("results");
				if (results != null) {
					for (JsonNode result: results) {
						JsonNode openFDA = result.get("openfda").get("substance_name");
						if (openFDA != null || nonOpenFDA) {
							String record = result.toString();
							logger.debug(record);
							SendJMSMessage(record);
						}
						count++;skip++;
					}
				}
			}
			logger.info("Current record count: " + count);
		}
		logger.info("Total records retrieved: " + count);
	}
	
	/**
	 * Retreives data from the given URL with the given set limit and records to skip
	 * @param baseURL
	 * @param limit
	 * @param skip
	 * @return the data in JSON format
	 */
	protected JsonNode getData(String baseURL, int limit, int skip) {
		JsonNode jsonNode = null;
		InputStream is = null;
		String url = formatURL(baseURL, limit, skip);
		logger.info("Get URL: " + url);
		try {
			is = new URL(url).openStream();
			jsonNode = mapper.readTree(is);
		} catch (Exception e) {
			logger.error("Error with API call " + url, e);
		} finally {
			IOUtils.closeQuietly(is);
		}
		return jsonNode;
	}
	
	/**
	 * @return a formatted date range String as exepected by the openFDA API
	 */
	private String formatDateRange() {
		return String.format(dateRange, startYear, endYear);
	}
	
	/**
	 * Formats the URL to match what openFDA expects
	 * @param baseURL
	 * @param limit
	 * @param skip
	 * @return
	 */
	private String formatURL(String baseURL, int limit, int skip) {
		return String.format("%s%s%d%s%d", baseURL, "&limit=", limit, "&skip=", skip);
	}
	
	/**
	 * Retrieves the count of records returned from a query to the openFDA API
	 * @return
	 */
	private int getTotal() {
		JsonNode jsonNode = getData(baseURL, 1, 0);
		return jsonNode.get("meta").get("results").get("total").asInt();
	}

	/**
	 * Return the base URL of the target API
	 * @return
	 */
	@UserConfigured(value = "https://api.fda.gov/drug/enforcement.json", description = "The REST endpoint for the openFDA API",
			flexValidator = { "StringValidator minLength=8 maxLength=255" })
	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * Set the base URL for the target API
	 * @param baseURL
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * Return the starting year of the date range query
	 * @return
	 */
	public int getStartYear() {
		return startYear;
	}

	/**
	 * Set the starting year of the date range query
	 * @param startYear
	 */
	@UserConfigured(value = "2004", description = "The start year to search form (inclusive)", 
			flexValidator = "NumberValidator minValue=2000 maxValue=2050")
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	/**
	 * Return the ending year of the date range query (inclusive)
	 * @return
	 */
	public int getEndYear() {
		return endYear;
	}

	/**
	 * Set the ending year of the date range query (inclusive)
	 * @param endYear
	 */
	@UserConfigured(value = "2015", description = "The end year to search through (inclusive)", 
			flexValidator = "NumberValidator minValue=2000 maxValue=2050")
	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}

	/**
	 * Return true if all records including those without the opendFDA section are processed, false otherwise
	 * @return
	 */
	public boolean isNonOpenFDA() {
		return nonOpenFDA;
	}

	/**
	 * Set true if you want all records including those without the openFDA section
	 * @param nonOpenFDA
	 */
	@UserConfigured(value = "false", description = "Include records without the openfda section", 
			flexValidator = { "RegExpValidator expression=true|false" })
	public void setNonOpenFDA(boolean nonOpenFDA) {
		this.nonOpenFDA = nonOpenFDA;
	}

	/**
	 * Final chance to cleanup resources
	 */
	@Override
	public void terminate() {
	}

}
