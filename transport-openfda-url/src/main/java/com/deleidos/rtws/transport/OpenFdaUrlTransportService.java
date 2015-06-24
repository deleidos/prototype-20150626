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

	@Override
	public void initialize() {
		super.initialize();
		dateRange = formatDateRange();
		baseURL = baseURL + dateRange;
	}

	@Override
	public void dispose() {
		super.dispose();
	}

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
	
	private String formatDateRange() {
		return String.format(dateRange, startYear, endYear);
	}
	
	private String formatURL(String baseURL, int limit, int skip) {
		return String.format("%s%s%d%s%d", baseURL, "&limit=", limit, "&skip=", skip);
	}
	
	private int getTotal() {
		JsonNode jsonNode = getData(baseURL, 1, 0);
		return jsonNode.get("meta").get("results").get("total").asInt();
	}
	
	public static void main(String[] args) {
		OpenFdaUrlTransportService x = new OpenFdaUrlTransportService();
		x.initialize();
		x.execute();
	}

	@UserConfigured(value = "https://api.fda.gov/drug/enforcement.json", description = "The REST endpoint for the openFDA API",
			flexValidator = { "StringValidator minLength=8 maxLength=255" })
	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public int getStartYear() {
		return startYear;
	}

	@UserConfigured(value = "2004", description = "The start year to search form (inclusive)", 
			flexValidator = "NumberValidator minValue=2000 maxValue=2050")
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getEndYear() {
		return endYear;
	}

	@UserConfigured(value = "2015", description = "The end year to search through (inclusive)", 
			flexValidator = "NumberValidator minValue=2000 maxValue=2050")
	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}

	public boolean isNonOpenFDA() {
		return nonOpenFDA;
	}

	@UserConfigured(value = "false", description = "Include records without the openfda section", 
			flexValidator = { "RegExpValidator expression=true|false" })
	public void setNonOpenFDA(boolean nonOpenFDA) {
		this.nonOpenFDA = nonOpenFDA;
	}

	@Override
	public void terminate() {
	}

}
