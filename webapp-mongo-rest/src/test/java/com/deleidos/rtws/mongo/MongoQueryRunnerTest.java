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
    	
    	System.out.println(record.get("results").getClass());
    	assertEquals(50, ((BasicDBList)record.get("results")).size());
    }
    
    @Test
    public void testQueryFilterForDrug() throws UnknownHostException, MongoException {
    	assertEquals(50, collection.getCount());
    	MongoQueryRunner qr = new MongoQueryRunner();
    	String result = qr.query("localhost", "dbname", "fda_enforcement", null, null, null);
    	DBObject record = (DBObject) JSON.parse(result);
    	
    	assertTrue(record.containsField("count"));
    	assertEquals(50, record.get("count"));
    	
    	System.out.println(record.get("results").getClass());
    	assertEquals(50, ((BasicDBList)record.get("results")).size());
    }

}
