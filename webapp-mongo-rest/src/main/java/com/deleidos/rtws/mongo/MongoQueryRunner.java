package com.deleidos.rtws.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "/mongo", description = "REST API for DigitalEdge/openFDA Mongo database")
@Path("/mongo")
public class MongoQueryRunner {

	private static final Logger logger = Logger.getLogger(MongoQueryRunner.class
			.getName());
	
	private static Map<String, Mongo> mongoClients = new HashMap<String, Mongo>();
	
	private static Mongo getMongoClient(String host) {
		if (!mongoClients.containsKey(host)) {
			try {
				Mongo mongoClient = new Mongo(host, 27017);
				mongoClients.put(host, mongoClient);
			} catch (UnknownHostException e) {
				logger.error("Failed to connect to MongoDB", e);
			} catch (MongoException e) {
				logger.error("Failed to connect to MongoDB", e);
			}
		}
		return mongoClients.get(host);
	}
	
	@ApiOperation(value = "Provides basic read-only query access to the recall data informaton in the MongoDB",
				notes = "The data in this MongoDB is obtained from the openFDA API recall information at https://api.fda.gov/drug/event.json. " +
						"Please see https://open.fda.gov/drug/enforcement/ for more information.  The data stored is from 2004-2015 and enriched " +
						"by DigitalEdge http://www.deleidos.com/ to normalize the \"distribution_pattern\" field for use in the FDA recall prototype " +
						"application http://openfda.deleidos.com  This query method returns a JSON String contaning the database records that match the " +
						"query parameters as well as a count of number of records returned.  For more information on the filter paramater syntax please " +
						"see the MongoDB documentation at http://docs.mongodb.org/manual/reference/method/db.collection.find/",
			    response = String.class)
	@Path("/query")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String query(@ApiParam(value = "The Mongo hostname or IP address", defaultValue="mongo", required = true) @QueryParam("host") String host,
			@ApiParam(value = "The Mongo database name", defaultValue="dbname", required = true) @QueryParam("database") String databaseName,
			@ApiParam(value = "The Mongo collection name", defaultValue="fda_enforcement", required = true) @QueryParam("collection") String collectionName,
			@ApiParam(value = "A standard Mongo JSON formatted query operator to filter results, if omitted the query is for all records ({})", required = false) @QueryParam("filter") String filter,
			@ApiParam(value = "A list of comma separated field names to return in the result, if omitted all fields are returned", required = false) @QueryParam("fields") String fields,
			@ApiParam(value = "A limit for the number of records to return, if omitted all records matching the query are returned", required = false) @QueryParam("limit") Integer limit) {
		
		Mongo mongoClient = getMongoClient(host);
		DB mongoDatabase = mongoClient.getDB(databaseName);
		DBCollection collection = mongoDatabase.getCollection(collectionName);
		
		DBObject query = filter == null ? new BasicDBObject() : (DBObject) JSON.parse(filter);
		
		DBObject fieldNames = null;
		if (fields != null && fields.length() > 0) {
			String[] splitFields = fields.split(",");
			fieldNames = new BasicDBObject(splitFields.length+1);
			fieldNames.put("_id", 0);
			for (String f: splitFields) {
				fieldNames.put(f, 1);
			}
		}
		
		DBCursor cursor = (fieldNames == null) ? collection.find(query) : collection.find(query, fieldNames);
		if (limit != null && limit != 0) {
			cursor.limit((limit));
		}
		
		List<DBObject> results = new ArrayList<DBObject>();
		if (cursor.hasNext()) {
			while (cursor.hasNext()) {
				DBObject dbObj = cursor.next();
				results.add(dbObj);
			}
		}
		DBObject response = new BasicDBObject();
		response.put("count", results.size());
		response.put("results", results);
		return response.toString();
	}
	
	@ApiOperation(value = "Aggregates the number of recalls for a given manufacturer by state and returns the count of recalls per state",
			notes = "This method accesses the same MongoDB recall records as described in the \"query\" method",
		    response = String.class)
	@Path("/statecount")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String stateCount(@ApiParam(value = "The Mongo hostname or IP address", defaultValue="mongo", required = true) @QueryParam("host") String host,
			@ApiParam(value = "The Mongo database name", defaultValue="dbname", required = true) @QueryParam("database") String databaseName,
			@ApiParam(value = "The Mongo collection name", defaultValue="fda_enforcement", required = true) @QueryParam("collection") String collectionName,
			@ApiParam(value = "The name of the manufacturer, if omitted all recalls for all manufacturers are counted by state/region", required = false) @QueryParam("manufacturer") String manufacturer) {
		
		logger.info("manufacturer="+manufacturer);
		Mongo mongoClient = getMongoClient(host);
		DB mongoDatabase = mongoClient.getDB(databaseName);
		DBCollection collection = mongoDatabase.getCollection(collectionName);
		
		DBObject query = new BasicDBObject();
		if (manufacturer != null) {
			query.put("openfda.manufacturer_name.0.0", manufacturer);
		}
		DBObject fieldNames = new BasicDBObject();
		fieldNames.put("_id", 0);
		fieldNames.put("recall_area", 1);
		fieldNames.put("openfda.brand_name", 1);
		fieldNames.put("city", 1);
		fieldNames.put("state", 1);
		fieldNames.put("country", 1);
		
		HashMap<String, Integer> stateCount = new HashMap<String, Integer>();
		HashSet<String> brandNames = new HashSet<String>();
		String city = null;
		String state = null;
		String country = null;
		DBCursor cursor = collection.find(query, fieldNames);
		if (cursor.hasNext()) {
			while (cursor.hasNext()) {
				DBObject record = cursor.next();
				if (manufacturer != null && city == null) {
					city = (record.get("city") == null) ? null : record.get("city").toString();
					state = (record.get("state") == null) ? null : record.get("state").toString();
					country = (record.get("country") == null) ? null : record.get("country").toString();
				}
				
				DBObject openfda = (DBObject)record.get("openfda");
				BasicDBList bnList = (BasicDBList)openfda.get("brand_name");
				BasicDBList brandList = (BasicDBList)bnList.get(0);
				if (brandList != null) {
					for (Object brandObj: brandList) {
						brandNames.add(brandObj.toString());
					}
				}
				
				BasicDBList stateNames = (BasicDBList)record.get("recall_area");
				if (stateNames != null) {
					for (Object stateObj: stateNames) {
						String stateName = stateObj.toString();
						int count = stateCount.get(stateName) == null ? 0 : stateCount.get(stateName).intValue();
						stateCount.put(stateName, ++count);
					}
				}
			}
		}
		
		List<DBObject> results = new ArrayList<DBObject>();
		for (Map.Entry<String, Integer> entry : stateCount.entrySet()) {
		    String stateName = entry.getKey();
		    Integer count = entry.getValue();
		    DBObject stateAndCount = new BasicDBObject();
		    stateAndCount.put("state", stateName);
		    stateAndCount.put("count", count);
		    results.add(stateAndCount);
		}
		DBObject response = new BasicDBObject();
		response.put("manufacturer", manufacturer);
		BasicDBList brandList = new BasicDBList();
		Iterator<String> brandIter = brandNames.iterator();
	    while (brandIter.hasNext()){
	    	brandList.add(brandIter.next());
	    }
	    response.put("city", city);
	    response.put("state", state);
	    response.put("country", country);
	    response.put("brand_names", brandList);
		response.put("count", results.size());
		response.put("results", results);
		return response.toString();
	}
	
	@ApiOperation(value = "Aggregates the number of recalls for a given manufacturer by drug and returns the count of recalls per drug",
			notes = "This method accesses the same MongoDB recall records as described in the \"query\" method",
		    response = String.class)
	@Path("/drugcount")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String drugCount(@ApiParam(value = "The Mongo hostname or IP address", defaultValue="mongo", required = true) @QueryParam("host") String host,
			@ApiParam(value = "The Mongo database name", defaultValue="dbname", required = true) @QueryParam("database") String databaseName,
			@ApiParam(value = "The Mongo collection name", defaultValue="fda_enforcement", required = true) @QueryParam("collection") String collectionName,
			@ApiParam(value = "The name of the manufacturer, if omitted all recalls for all manufacturers are counted by drug name", required = false) @QueryParam("manufacturer") String manufacturer) {
		
		logger.info("manufacturer="+manufacturer);
		Mongo mongoClient = getMongoClient(host);
		DB mongoDatabase = mongoClient.getDB(databaseName);
		DBCollection collection = mongoDatabase.getCollection(collectionName);
		
		DBObject query = new BasicDBObject();
		if (manufacturer != null) {
			query.put("openfda.manufacturer_name.0.0", manufacturer);
		}
		DBObject fieldNames = new BasicDBObject();
		fieldNames.put("_id", 0);
		fieldNames.put("recall_area", 1);
		fieldNames.put("openfda.brand_name", 1);
		
		HashMap<String, Integer> drugCount = new HashMap<String, Integer>();
		HashMap<String, HashSet<String>> drugToStates = new HashMap<String, HashSet<String>>();
		DBCursor cursor = collection.find(query, fieldNames);
		if (cursor.hasNext()) {
			while (cursor.hasNext()) {
				DBObject record = cursor.next();
				BasicDBList areaList = (BasicDBList)record.get("recall_area");
				List<String> states = Arrays.asList(areaList.toArray(new String[areaList.size()]));
				
				DBObject openfda = (DBObject)record.get("openfda");
				BasicDBList bnList = (BasicDBList)openfda.get("brand_name");
				BasicDBList brandList = (BasicDBList)bnList.get(0);
				if (brandList != null) {
					for (Object brandObj: brandList) {
						String drugName = brandObj.toString();
						int count = !drugCount.containsKey(drugName) ? 0 : drugCount.get(drugName).intValue();
						drugCount.put(drugName, ++count);
						
						if (!drugToStates.containsKey(drugName)) {
							drugToStates.put(drugName, new HashSet<String>());
						}
						drugToStates.get(drugName).addAll(states);
					}
				}
			}
		}
		
		List<DBObject> results = new ArrayList<DBObject>();
		for (Map.Entry<String, Integer> entry : drugCount.entrySet()) {
		    String drugName = entry.getKey();
		    Integer count = entry.getValue();
		    DBObject result = new BasicDBObject();
		    result.put("drug_name", drugName);
		    result.put("count", count);
		    result.put("recall_area", drugToStates.get(drugName));
		    results.add(result);
		}
		DBObject response = new BasicDBObject();
		response.put("manufacturer", manufacturer);
		response.put("count", results.size());
		response.put("results", results);
		return response.toString();
	}
	
}
