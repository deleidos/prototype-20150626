package com.deleidos.rtws.transport;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.junit.Test;

import com.deleidos.rtws.transport.OpenFdaUrlTransportService;

public class OpenFdaUrlTransportServiceTest {

	@Test
	public void testGetData10Records() {
		OpenFdaUrlTransportService transport = new OpenFdaUrlTransportService();
		JsonNode jsonNode = transport.getData("https://api.fda.gov/drug/enforcement.json?search=report_date:[20040101+TO+20151231]", 10, 0);
		assertNotNull(jsonNode);
		ArrayNode results = (ArrayNode)jsonNode.get("results");
		assertNotNull(results);
		assertTrue(results.size() == 10);
	}
	
	@Test
	public void testGetData100Records() {
		OpenFdaUrlTransportService transport = new OpenFdaUrlTransportService();
		JsonNode jsonNode = transport.getData("https://api.fda.gov/drug/enforcement.json?search=report_date:[20040101+TO+20151231]", 100, 0);
		assertNotNull(jsonNode);
		ArrayNode results = (ArrayNode)jsonNode.get("results");
		assertNotNull(results);
		assertTrue(results.size() == 100);
	}
	
	@Test
	public void testGetDataInvalidURL() {
		OpenFdaUrlTransportService transport = new OpenFdaUrlTransportService();
		JsonNode jsonNode = transport.getData("https://api.fda.gov/bogus", 10, 0);
		assertNull(jsonNode);
	}
}
