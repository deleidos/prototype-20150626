package com.deleidos.enrichment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class RecallAreaNormalizerTest {

	@Test
	public void findStateNameMatchesTest() {
		RecallAreaNormalizer normalizer = new RecallAreaNormalizer();
		normalizer.loadLookup();
		List<String> states = normalizer.findMatches("Distributed in Florida, California, Minnesota, and Maine", RecallAreaNormalizer.STATE_NAMES);
		assertNotNull(states);
		assertTrue(states.size() == 4);
		assertTrue(states.contains("Florida"));
		assertTrue(states.contains("California"));
		assertTrue(states.contains("Minnesota"));
		assertTrue(states.contains("Maine"));
	}
	
	@Test
	public void findPostalCodeMatchesTest() {
		RecallAreaNormalizer normalizer = new RecallAreaNormalizer();
		normalizer.loadLookup();
		List<String> postalCodes = normalizer.findMatches("Distributed in FL, CA, MN, and ME", RecallAreaNormalizer.POSTAL_CODES);
		assertNotNull(postalCodes);
		assertTrue(postalCodes.size() == 4);
		List<String> states = normalizer.lookupStateNames(postalCodes);
		assertTrue(states.contains("Florida"));
		assertTrue(states.contains("California"));
		assertTrue(states.contains("Minnesota"));
		assertTrue(states.contains("Maine"));
	}
}
