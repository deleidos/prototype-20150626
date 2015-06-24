package com.deleidos.rtws.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class MongoQueryRunnerTest {

	private static MongodExecutable _mongodExe;
    private static MongodProcess _mongod;
    private static Mongo mongoClient;
    private static DBCollection collection;
    
    @BeforeClass
    public static void setUp() throws Exception {
        MongodStarter runtime = MongodStarter.getDefaultInstance();
        _mongodExe = runtime.prepare(new MongodConfig(Version.Main.V2_3, 27107, Network.localhostIsIPv6()));
        _mongod = _mongodExe.start();
        seedDatabase();
    }

    @AfterClass
    public static void tearDown() throws Exception {
    	mongoClient.close();
    	_mongod.stop();
        _mongodExe.stop();
    }
    
    public static void seedDatabase() throws MongoException, IOException {
    	mongoClient = new Mongo("localhost", 27017);
    	DB database = mongoClient.getDB("dbname");
    	collection = database.getCollection("fda_enforcement");
    	collection.remove(new BasicDBObject());
    	
    	assertEquals(0, collection.getCount());
    	BufferedReader br = new BufferedReader(new FileReader("src/test/resources/test_records.json"));
    	String line = null;
    	while ((line = br.readLine()) != null) {
    		DBObject record = (DBObject) JSON.parse(line);
    		collection.insert(record);
    	}
    	br.close();
    	assertEquals(50, collection.getCount());
    }
    
    @Test
    public void testQueryAllRecords() throws UnknownHostException, MongoException {
    	MongoQueryRunner qr = new MongoQueryRunner();
    	String result = qr.query("localhost", "dbname", "fda_enforcement", null, null, null);
    	DBObject record = (DBObject) JSON.parse(result);
    	
    	assertTrue(record.containsField("count"));
    	assertEquals(50, record.get("count"));
    	assertTrue(record.containsField("results"));
    	assertEquals(50, ((BasicDBList)record.get("results")).size());
    }
    
    @Test
    public void testQueryFilterDrugName() throws UnknownHostException, MongoException {
    	assertEquals(50, collection.getCount());
    	MongoQueryRunner qr = new MongoQueryRunner();
    	String result = qr.query("localhost", "dbname", "fda_enforcement", "{\"openfda.substance_name.0.0\":\"CARBOPLATIN\"}", null, null);
    	DBObject record = (DBObject) JSON.parse(result);
    	
    	assertTrue(record.containsField("count"));
    	assertEquals(1, record.get("count"));
    	assertTrue(record.containsField("results"));
    	assertEquals(1, ((BasicDBList)record.get("results")).size());
    	
    	BasicDBList results = (BasicDBList)record.get("results");
    	DBObject result1 = (DBObject)results.get(0);
    	DBObject openfda = (DBObject)result1.get("openfda");
    	BasicDBList list1 = (BasicDBList)openfda.get("substance_name");
    	BasicDBList list2 = (BasicDBList)list1.get(0);
    	assertEquals("CARBOPLATIN", list2.get(0));
    }
    
    @Test
    public void testQueryFilterRecallArea() throws UnknownHostException, MongoException {
    	assertEquals(50, collection.getCount());
    	MongoQueryRunner qr = new MongoQueryRunner();
    	String result = qr.query("localhost", "dbname", "fda_enforcement", "{\"recall_area\":\"California\"}", null, null);
    	DBObject record = (DBObject) JSON.parse(result);
    	
    	assertTrue(record.containsField("count"));
    	assertEquals(1, record.get("count"));
    	assertTrue(record.containsField("results"));
    	assertEquals(1, ((BasicDBList)record.get("results")).size());
    	
    	BasicDBList results = (BasicDBList)record.get("results");
    	DBObject result1 = (DBObject)results.get(0);
    	assertTrue(result1.get("recall_area").toString().indexOf("California") > -1);
    }
    
    @Test
    public void testQueryLimitFields() throws UnknownHostException, MongoException {
    	assertEquals(50, collection.getCount());
    	MongoQueryRunner qr = new MongoQueryRunner();
    	String result = qr.query("localhost", "dbname", "fda_enforcement", "{\"recall_area\":\"California\"}", "recall_area", null);
    	DBObject record = (DBObject) JSON.parse(result);
    	
    	assertTrue(record.containsField("count"));
    	assertEquals(1, record.get("count"));
    	assertTrue(record.containsField("results"));
    	assertEquals(1, ((BasicDBList)record.get("results")).size());
    	
    	BasicDBList results = (BasicDBList)record.get("results");
    	DBObject result1 = (DBObject)results.get(0);
    	assertEquals(1, result1.keySet().size());
    	assertTrue(result1.containsField("recall_area"));
    }
    
    @Test
    public void testQueryLimitRecordCount() throws UnknownHostException, MongoException {
    	MongoQueryRunner qr = new MongoQueryRunner();
    	String result = qr.query("localhost", "dbname", "fda_enforcement", null, null, "10");
    	DBObject record = (DBObject) JSON.parse(result);
    	
    	assertTrue(record.containsField("count"));
    	assertEquals(10, record.get("count"));
    	assertTrue(record.containsField("results"));
    	assertEquals(10, ((BasicDBList)record.get("results")).size());
    }
    
    @Test
    public void testStateCount() throws UnknownHostException, MongoException {
    	MongoQueryRunner qr = new MongoQueryRunner();
    	String result = qr.stateCount("localhost", "dbname", "fda_enforcement", "Baxter Healthcare Corporation");
    	DBObject record = (DBObject) JSON.parse(result);
    	
    	assertTrue(record.containsField("count"));
    	assertEquals(1, record.get("count"));
    	assertTrue(record.containsField("results"));
    	assertEquals(1, ((BasicDBList)record.get("results")).size());
    	
    	assertEquals("Baxter Healthcare Corporation", record.get("manufacturer").toString());
    	assertEquals("Deerfield", record.get("city").toString());
    	assertEquals("IL", record.get("state").toString());
    	assertEquals("US", record.get("country").toString());
    	
    	BasicDBList brandList = (BasicDBList)record.get("brand_names");
    	assertEquals(2, brandList.size());
    	assertEquals("DIANEAL LOW CALCIUM WITH DEXTROSE", brandList.get(0).toString());
    	assertEquals("DEXTROSE", brandList.get(1).toString());
    	
    	BasicDBList results = (BasicDBList)record.get("results");
    	assertEquals(1, results.size());
    	DBObject result2 = (DBObject)results.get(0);
    	assertEquals("Nationwide", result2.get("state"));
    	assertEquals(3, result2.get("count"));
    }
}
