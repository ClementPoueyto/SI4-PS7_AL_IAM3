package com.aliam3.polyvilleactive.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aliam3.polyvilleactive.db.MockAPI;
import com.aliam3.polyvilleactive.dsl.events.transportation.Avance;
import com.aliam3.polyvilleactive.dsl.events.transportation.Panne;
import com.aliam3.polyvilleactive.dsl.events.transportation.Rempli;
import com.aliam3.polyvilleactive.dsl.events.transportation.Retard;
import com.aliam3.polyvilleactive.model.incidents.transportation.BusAhead;
import com.aliam3.polyvilleactive.model.transport.Journey;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.service.JourneyService;

public class BusAheadTest {

	BusAhead avance;

	@BeforeEach
	void setUp() {
		avance = new BusAhead(
				"Pontoise / Versailles R. Gauche / St-Quentin en Y. - Versailles Ch. / Dourdan la F. / St-Martin d'E",
				ModeTransport.TRAIN);
	}

	@Test
	public void triggersTest() {
		Avance v = new Avance(ModeTransport.TRAIN);
		assertTrue(avance.triggers(v));
		assertFalse(avance.triggers(new Retard(null)));
		assertFalse(avance.triggers(new Panne(null)));
		assertFalse(avance.triggers(new Rempli(null)));
	}

	@Test
	public void affectsTest() {
		MockAPI mockAPI = new MockAPI();
		JourneyService journeyService = new JourneyService();
		Journey j = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json")).get(0);
		assertTrue(avance.affects(j));
	}
}
