package com.aliam3.polyvilleactive.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aliam3.polyvilleactive.db.MockAPI;
import com.aliam3.polyvilleactive.dsl.events.transportation.Avance;
import com.aliam3.polyvilleactive.dsl.events.transportation.Panne;
import com.aliam3.polyvilleactive.dsl.events.transportation.Rempli;
import com.aliam3.polyvilleactive.dsl.events.transportation.Retard;
import com.aliam3.polyvilleactive.model.incidents.transportation.FullPeople;
import com.aliam3.polyvilleactive.model.transport.Journey;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.service.JourneyService;

public class FullPeopleTest {

	FullPeople people;

	@BeforeEach
	void setUp() {
		people = new FullPeople(
				"Pontoise / Versailles R. Gauche / St-Quentin en Y. - Versailles Ch. / Dourdan la F. / St-Martin d'E",
				ModeTransport.TRAIN);
	}

	@Test
	public void triggersTest() {
		Rempli r = new Rempli(ModeTransport.TRAIN);
		assertTrue(people.triggers(r));
		assertFalse(people.triggers(new Retard(null)));
		assertFalse(people.triggers(new Panne(null)));
		assertFalse(people.triggers(new Avance(null)));
	}

	@Test
	public void affectsTest() {
		MockAPI mockAPI = new MockAPI();
		JourneyService journeyService = new JourneyService();
		Journey j = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json")).get(0);
		assertTrue(people.affects(j));
	}
}
