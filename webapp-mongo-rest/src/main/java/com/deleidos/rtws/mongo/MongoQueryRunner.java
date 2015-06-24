package com.deleidos.rtws.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
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
	
	@Path("/query")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String query(@QueryParam("host") String host,
			@QueryParam("database") String databaseName,
			@QueryParam("collection") String collectionName,
			@QueryParam("filter") String filter,
			@QueryParam("fields") String fields,
			@QueryParam("limit") String limit) {
		
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
		if (limit != null && !limit.isEmpty()) {
			cursor.limit(Integer.parseInt(limit));
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
	
	@Path("/statecount")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String statecount(@QueryParam("host") String host,
			@QueryParam("database") String databaseName,
			@QueryParam("collection") String collectionName,
			@QueryParam("manufacturer") String manufacturer) {
		
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
		
		HashMap<String, Integer> stateCount = new HashMap<String, Integer>();
		DBCursor cursor = collection.find(query, fieldNames);
		if (cursor.hasNext()) {
			while (cursor.hasNext()) {
				DBObject dbObj = cursor.next();
				BasicDBList stateNames = (BasicDBList)dbObj.get("recall_area");
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
		response.put("count", results.size());
		response.put("results", results);
		return response.toString();
	}
	
	@Path("/aggregate")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String aggregate(@QueryParam("host") String host,
			@QueryParam("database") String databaseName,
			@QueryParam("collection") String collectionName,
			@QueryParam("group") String match,
			@QueryParam("unwind") String unwind,
			@QueryParam("group") String group,
			@QueryParam("limit") String limit) {
		
		// need the newer version of the mongo driver to do this
		return null;
	}
		
}
