package com.deleidos.enrichment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.deleidos.rtws.core.framework.EnrichmentDefinition;
import com.deleidos.rtws.core.framework.EnrichmentProperty;
import com.deleidos.rtws.core.framework.processor.AbstractEnrichmentProcessor;
import com.deleidos.rtws.core.framework.processor.EnrichmentAction;
import com.deleidos.rtws.core.framework.processor.ParameterList;

/**
 * Processes the free form "distribution_pattern" field in the enforcment data to produce a new normalized 
 * list of recall areas for mapping purposes.  The normalized list is to include either "Nationwide", and 
 * array of state names ([California, New York, Florida, etc.]), or "Unknown".
 */
@EnrichmentDefinition(type = "recall_area_normalizer", description = "Normalizes the free form distribution pattern descriptions for recalled FDA drugs.", 
properties = { @EnrichmentProperty(
		name="parameters",
		description="The pre-normalized distribution pattern for the recalled drug",
		type="java.lang.String"
	) })
public class RecallAreaNormalizer extends AbstractEnrichmentProcessor {

	/**
	 * Array of standard US Postal Codes
	 */
	protected static final String[] POSTAL_CODES = 
		{ "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "DC", 
		  "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", 
		  "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", 
		  "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", 
		  "OK", "OR", "PA", "PR", "RI", "SC", "SD", "TN", "TX", 
		  "UT", "VT", "VA", "WA", "WV", "WI", "WY" };
	
	/**
	 * Array of standar US State Names
	 */
	protected static final String[] STATE_NAMES = 
		{ "Alabama", "Alaska", "Arizona", "Arkansas", "California", 
		"Colorado", "Connecticut", "Delaware", "Dist. of Columbia", "Florida", 
		"Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", 
		"Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", 
		"Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", 
		"New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", 
		"North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Puerto Rico", 
		"Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", 
		"Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming" };
	
	/**
	 * Post Code to State Name lookup map
	 */
	protected static HashMap<String, String> stateNameLookup = new HashMap<String, String>();

	/**
	 * No-arg constructor
	 */
	public RecallAreaNormalizer() {
		super();
	}	
	
	/**
	 * Returns the Type name to be listed in DE Enrichment listing
	 */
	@Override
	public String getType() {
		return "recall_area_normalizer";
	}
	
	/**
	 * One time initialization step, load state lookup map
	 */
	@Override
	public void initialize() {
		super.initialize();
		loadLookup();
	}
	
	/**
	 * Loads the lookup map to associate Postal Codes to State Names
	 */
	protected void loadLookup() {
		for (int i=0; i<POSTAL_CODES.length; i++) {
			stateNameLookup.put(POSTAL_CODES[i], STATE_NAMES[i]);
		}
	}

	/**
	 * The public method invoked by the DigitalEdge pipeline that performs the actual enrichment.
	 * The free form distribution_pattern field from the openFDA data is processed as following: 
	 * If the term "Nationwide" is found in the field "Nationwide" is returned, if a list of Postal
	 * Codes or State names are found a list of State names is returned, otherwise "Unknown" is returned.
	 * @param action details to assist in tailoring the enrichment process
	 * @param parameters the raw input fields to be enriched
	 */
	@Override
	public JSONObject buildEnrichedElement(EnrichmentAction action, ParameterList parameters) {
		String distribution_pattern = parameters.get(0, String.class);
		if (distribution_pattern == null || distribution_pattern.isEmpty()) {
			return null;
		} else {
			JSONArray recallArea = new JSONArray();
			if (distribution_pattern.contains("Nationwide") || 
				distribution_pattern.contains("nationwide") || 
				distribution_pattern.contains("NATIONWIDE")){
				recallArea.add("Nationwide");
			} else {
				List<String> stateMatches = findMatches(distribution_pattern, STATE_NAMES);
				if (stateMatches.size() == 00) {
					List<String> postalMatches = findMatches(distribution_pattern, POSTAL_CODES);
					if (postalMatches.size() > 0) {
						stateMatches = lookupStateNames(postalMatches);
					}
				}
				if (stateMatches.size() > 0) {
					for (String state: stateMatches) {
						recallArea.add(state);
					}
				} else {
					recallArea.add("Unknown");
				}
			}
			return new JSONObject().element("recall_area", recallArea);
		}
	}
	
	/**
	 * Finds which fields in the searchFields array are contained with the given searchString
	 * @param searchString
	 * @param searchFields
	 * @return the list of searchFields that were found in the search String
	 */
	protected List<String> findMatches(String searchString, String[] searchFields) {
		List<String> matches = new ArrayList<String>();
		for (String searchField: searchFields) {
			if (searchString.contains(searchField)) {
				matches.add(searchField);
			}
		}
		return matches;
	}
	
	/**
	 * Retrieves the US State Name associated with the given Postal Code
	 * @param postalCodes
	 * @return the matching State Name
	 */
	protected List<String> lookupStateNames(List<String> postalCodes) {
		List<String> codes = new ArrayList<String>();
		for (String code: postalCodes) {
			codes.add(stateNameLookup.get(code));
		}
		return codes;
	}
}
