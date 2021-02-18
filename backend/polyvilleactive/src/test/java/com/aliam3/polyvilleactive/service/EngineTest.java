package com.aliam3.polyvilleactive.service;

import com.aliam3.polyvilleactive.MemoryRule;
import com.aliam3.polyvilleactive.db.MockAPI;
import com.aliam3.polyvilleactive.dsl.Action;
import com.aliam3.polyvilleactive.dsl.Priorite;
import com.aliam3.polyvilleactive.dsl.Prohibition;
import com.aliam3.polyvilleactive.dsl.Regle;
import com.aliam3.polyvilleactive.dsl.events.transportation.Avance;
import com.aliam3.polyvilleactive.dsl.events.transportation.Retard;
import com.aliam3.polyvilleactive.dsl.events.weather.SnowEvent;
import com.aliam3.polyvilleactive.model.incidents.transportation.Delay;
import com.aliam3.polyvilleactive.model.incidents.transportation.IncidentTransport;
import com.aliam3.polyvilleactive.model.incidents.weather.IncidentWeather;
import com.aliam3.polyvilleactive.model.incidents.weather.Snow;
import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.location.Place;
import com.aliam3.polyvilleactive.model.transport.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {

	private Engine engine;

	@BeforeEach
	void setUp() {
		engine = new Engine();
	}

	private Journey setup2sectionJourney(Transport transport1, Duration duration1, Transport transport2,
			Duration duration2) {
		Journey journey1 = new Journey();

		Map<ModeTransport, Long> mapTranspDuree = new HashMap<ModeTransport, Long>();
		mapTranspDuree.put(transport1.getModeTransport(), duration1.getSeconds()); // 75%
		mapTranspDuree.put(transport2.getModeTransport(), duration2.getSeconds()); // 25%
		journey1.setTransports(mapTranspDuree);

		Section section1 = new Section();
		section1.setFrom(new Place());
		section1.setTo(new Place());
		section1.setTransport(transport1);
		section1.setDuration(duration1.getSeconds());

		Section section2 = new Section();
		section2.setFrom(new Place());
		section2.setTo(new Place());
		section2.setTransport(transport2);
		section2.setDuration(duration2.getSeconds());

		List<Section> sections = new ArrayList<>();
		sections.add(section1);
		sections.add(section2);
		journey1.setSections(sections);
		return journey1;
	}

	void setupDSLrules() {
		// Setup for global priority
		List<Priorite> priorities = new ArrayList<>();
		priorities.add(new Priorite(1, ModeTransport.BUS));
		MemoryRule.changeGlobalPriorities(priorities);

		// Setup for local priority
		List<Priorite> priorities2 = new ArrayList<>();
		priorities2.add(new Priorite(-1, ModeTransport.BUS));

		MemoryRule.addLocalRules(new Regle(new Retard(ModeTransport.BUS), new Action(priorities2, new ArrayList<>())));
	}

	@Test
	void globalPriorityUsedWhenEventNotTriggerred() {
		setupDSLrules(); // +BUS if BUS delay, -BUS if not

		Duration fifteen = Duration.ofMinutes(15);
		Duration five = Duration.ofMinutes(5);

		// Setting up 1st journey
		Transport transport1 = new Transport(ModeTransport.METRO, fifteen, "15", "A");
		Transport transport2 = new Transport(ModeTransport.BUS, five, "12", "D"); // not delayed
		Journey lessBus = setup2sectionJourney(transport1, fifteen, transport2, five);

		// Setting up 2nd journey
		transport1 = new Transport(ModeTransport.METRO, five, "15", "A");
		transport2 = new Transport(ModeTransport.BUS, fifteen, "12", "D"); // not delayed
		Journey moreBus = setup2sectionJourney(transport1, five, transport2, fifteen);

		// Grouping things
		List<Journey> availableJourneys = new ArrayList<>();
		availableJourneys.add(lessBus);
		availableJourneys.add(moreBus);

		List<IncidentTransport> currentDelays = new ArrayList<>();
		List<IncidentWeather> currentWeather = new ArrayList<>();
		// currentDelays.add(delay);

		// Actual testing
		Journey selectedJourney = engine.selectJourney(0, null, availableJourneys, currentDelays, currentWeather,
				List.of(), false);
		assertEquals(moreBus, selectedJourney);
	}

	@Test
	void localPriorityUsedWhenEventWasTriggerred() {
		setupDSLrules(); // +BUS if BUS delay, -BUS if not

		Duration fifteen = Duration.ofMinutes(15);
		Duration five = Duration.ofMinutes(5);

		// Setting up failed journey
		Transport transport1 = new Transport(ModeTransport.METRO, fifteen, "15", "A");
		Transport transport2 = new Transport(ModeTransport.BUS, five, "11", "C");
		Journey failedJourney = setup2sectionJourney(transport1, five, transport2, fifteen); // DELAYED

		// Setting up delay
		Delay delay = new Delay(Duration.ofMinutes(2), "C", ModeTransport.BUS);

		// Setting up 1st journey
		transport1 = new Transport(ModeTransport.METRO, fifteen, "15", "A");
		transport2 = new Transport(ModeTransport.BUS, five, "12", "D"); // not delayed
		Journey lessBus = setup2sectionJourney(transport1, fifteen, transport2, five);

		// Setting up 2nd journey
		transport1 = new Transport(ModeTransport.METRO, five, "15", "A");
		transport2 = new Transport(ModeTransport.BUS, fifteen, "12", "D"); // not delayed
		Journey moreBus = setup2sectionJourney(transport1, five, transport2, fifteen);

		// Grouping things
		List<Journey> availableJourneys = new ArrayList<>();
		availableJourneys.add(lessBus);
		availableJourneys.add(moreBus);

		List<IncidentTransport> currentDelays = new ArrayList<>();
		List<IncidentWeather> currentWeather = new ArrayList<>();
		currentDelays.add(delay);

		// Actual testing
		Journey selectedJourney = engine.selectJourney(0, failedJourney, availableJourneys, currentDelays,
				currentWeather, List.of(), false);
		assertEquals(lessBus, selectedJourney);
	}

	@Test
	void testTriggeringDelayIsFound() {
		Duration dur = Duration.ofMinutes(5);
		Transport t1 = new Transport(ModeTransport.METRO, dur, "12", "A");
		Transport t2 = new Transport(ModeTransport.BUS, dur, "31", "Z");
		Journey failedJourney = setup2sectionJourney(t1, dur, t2, dur);

		Delay culprit = new Delay(dur, "Z", ModeTransport.BUS);
		Delay otherDelay = new Delay(dur, "F", ModeTransport.TRAIN);
		List<Incident> delays = new ArrayList<>();
		delays.add(culprit);
		delays.add(otherDelay);

		List<Incident> delayCandidates = engine.getIncidentsWhichAffected(failedJourney, delays);
		assertEquals(1, delayCandidates.size());
		assertEquals(culprit, delayCandidates.get(0));
	}

	@Test
	void testWhenNoTriggerNoDelayFound() {
		Duration dur = Duration.ofMinutes(5);
		Transport t1 = new Transport(ModeTransport.METRO, dur, "12", "A");
		Transport t2 = new Transport(ModeTransport.BUS, dur, "31", "Z");
		Journey failedJourney = setup2sectionJourney(t1, dur, t2, dur);

		Delay aDelay = new Delay(dur, "T1", ModeTransport.TRAMWAY);
		Delay otherDelay = new Delay(dur, "TGV2", ModeTransport.TRAIN);
		List<Incident> delays = new ArrayList<>();
		delays.add(aDelay);
		delays.add(otherDelay);

		List<Incident> delayCandidates = engine.getIncidentsWhichAffected(failedJourney, delays);
		assertEquals(0, delayCandidates.size());
	}

	@Test
	void testResultActionIsNotNull() {
		List<Regle> triggeredRules = new ArrayList<>();

		Priorite p1 = new Priorite(1, ModeTransport.BUS);
		Regle r1 = new Regle(new Retard(ModeTransport.METRO),
				new Action(new ArrayList<>(List.of(p1)), new ArrayList<>()));

		Priorite p2 = new Priorite(1, ModeTransport.TRAMWAY);
		Regle r2 = new Regle(new Retard(ModeTransport.TRAIN),
				new Action(new ArrayList<>(List.of(p2)), new ArrayList<>()));

		triggeredRules.add(r1);
		triggeredRules.add(r2);

		Action res = engine.getActionAfterIncidents(triggeredRules);
		assertNotNull(res);
	}

	@Test
	void getActionAfterIncidentsTest() {
		List<Regle> triggeredRules = new ArrayList<>();

		Priorite p1 = new Priorite(1, ModeTransport.BUS);
		Priorite p2 = new Priorite(1, ModeTransport.TRAMWAY);
		Priorite p3 = new Priorite(-1, ModeTransport.TRAIN);
		Prohibition p4 = new Prohibition(ModeTransport.METRO);

		Regle r1 = new Regle(new Retard(ModeTransport.METRO),
				new Action(new ArrayList<>(List.of(p1, p3)), new ArrayList<>(List.of(p4))));

		Regle r2 = new Regle(new Retard(ModeTransport.TRAIN),
				new Action(new ArrayList<>(List.of(p2)), new ArrayList<>()));

		triggeredRules.add(r1);
		triggeredRules.add(r2);

		Action res = engine.getActionAfterIncidents(triggeredRules);
		assertNotNull(res);
		assertTrue(res.getPriorites().size() == 3);
		assertTrue(res.getProhibitions().size() == 1);
		triggeredRules.clear();
		r2 = new Regle(new Avance(ModeTransport.TRAIN), new Action(new ArrayList<>(List.of(p2)), new ArrayList<>()));
		triggeredRules.add(r1);
		triggeredRules.add(r2);
		res = engine.getActionAfterIncidents(triggeredRules);
		assertNotNull(res);
		assertTrue(res.getPriorites().size() == 1);
		assertTrue(res.getProhibitions().size() == 0);

	}

	@Test
	void testTriggeredEventIsValidated() {
		Regle ruleToTrigger = new Regle(new Retard(ModeTransport.BUS), new Action());
		List<Regle> rules = new ArrayList<>();
		rules.add(ruleToTrigger);

		Delay culprit = new Delay(Duration.ofMinutes(5), null, ModeTransport.BUS);
		List<Incident> delays = new ArrayList<>();
		delays.add(culprit);

		List<Regle> triggeredCandidates = engine.findValidatedRules(rules, delays);
		assertEquals(1, triggeredCandidates.size());
		assertEquals(ruleToTrigger, triggeredCandidates.get(0));
	}

	@Test
	void testNonTriggeredEventIsNotValidated() {
		Regle ruleToNotTrigger = new Regle(new Retard(ModeTransport.BUS), new Action());
		List<Regle> rules = new ArrayList<>();
		rules.add(ruleToNotTrigger);

		Delay culprit = new Delay(Duration.ofMinutes(5), null, ModeTransport.TRAMWAY);
		List<Incident> delays = new ArrayList<>();
		delays.add(culprit);

		List<Regle> triggeredCandidates = engine.findValidatedRules(rules, delays);
		assertEquals(0, triggeredCandidates.size());
	}

	@Test
	void selectJourneyWithIncidentWeatherTest() {
		Duration fifteen = Duration.ofMinutes(15);
		Duration five = Duration.ofMinutes(5);

		Snow neige = new Snow("C", ModeTransport.BUS);

		// Setting up 1st journey
		Transport transport1 = new Transport(ModeTransport.METRO, fifteen, "15", "A");
		Transport transport2 = new Transport(ModeTransport.BUS, five, "11", "C");
		Journey willBeAffected = setup2sectionJourney(transport1, fifteen, transport2, five);

		// Setting up 2nd journey
		transport1 = new Transport(ModeTransport.METRO, five, "15", "A");
		transport2 = new Transport(ModeTransport.BUS, fifteen, "12", "D"); // different
		Journey wontBeAffected = setup2sectionJourney(transport1, five, transport2, fifteen);

		// Grouping things
		List<Journey> availableJourneys = new ArrayList<>();
		availableJourneys.add(willBeAffected);
		availableJourneys.add(wontBeAffected);

		List<IncidentTransport> currentDelays = new ArrayList<>();
		List<IncidentWeather> currentWeathers = new ArrayList<>();
		currentWeathers.add(neige);

		// Actual testing
		Journey selectedJourney = engine.selectJourney(0, null, availableJourneys, currentDelays, currentWeathers,
				List.of(), false);
		// assertTrue(selectedJourney==willBeAffected); //meteo affect mais vire pas le
		// premier
		assertEquals(willBeAffected, selectedJourney);
	}

	@Test
	void selectJourneyWithIncidentWeatherWithPrioritiesTest() {
		Duration fifteen = Duration.ofMinutes(15);
		Duration five = Duration.ofMinutes(5);

		Snow neige = new Snow("C", ModeTransport.BUS);

		// Setting up 1st journey
		Transport transport1 = new Transport(ModeTransport.METRO, fifteen, "15", "A");
		Transport transport2 = new Transport(ModeTransport.BUS, five, "11", "C");
		Journey willBeAffected = setup2sectionJourney(transport1, fifteen, transport2, five);

		// Setting up 2nd journey
		transport1 = new Transport(ModeTransport.METRO, five, "15", "A");
		transport2 = new Transport(ModeTransport.TRAIN, fifteen, "12", "D"); // different
		Journey wontBeAffected = setup2sectionJourney(transport1, five, transport2, fifteen);

		// Grouping things
		List<Journey> availableJourneys = new ArrayList<>();
		availableJourneys.add(willBeAffected);
		availableJourneys.add(wontBeAffected);

		List<IncidentTransport> currentDelays = new ArrayList<>();
		List<IncidentWeather> currentWeathers = new ArrayList<>();
		currentWeathers.add(neige);
		MemoryRule.addLocalRules(
				new Regle(new SnowEvent(), new Action(List.of(new Priorite(1, ModeTransport.TRAIN)), List.of())));

		// Actual testing
		Journey selectedJourney = engine.selectJourney(0, null, availableJourneys, currentDelays, currentWeathers,
				List.of(), false);
		// assertTrue(selectedJourney==willBeAffected); //meteo affect mais vire pas le
		// premier
		assertEquals(willBeAffected, selectedJourney);

		selectedJourney = engine.selectJourney(0, willBeAffected, availableJourneys, currentDelays, currentWeathers,
				List.of(), false);
		// assertTrue(selectedJourney==willBeAffected); //meteo affect mais vire pas le
		// premier
		assertEquals(wontBeAffected, selectedJourney);
	}

	@Test
	void anteriorDelayDoesNotAffectNewJourney() {
		Duration fifteen = Duration.ofMinutes(15);
		Duration five = Duration.ofMinutes(5);

		// Setting up failed journey
		Transport transport1 = new Transport(ModeTransport.METRO, fifteen, "15", "A");
		Transport transport2 = new Transport(ModeTransport.BUS, five, "11", "C");
		Journey failedJourney = setup2sectionJourney(transport1, five, transport2, fifteen);

		// Setting up delay
		Delay delay = new Delay(Duration.ofMinutes(2), "C", ModeTransport.BUS);

		// Setting up 1st journey
		transport1 = new Transport(ModeTransport.METRO, fifteen, "15", "A");
		transport2 = new Transport(ModeTransport.BUS, five, "11", "C");
		Journey willBeAffected = setup2sectionJourney(transport1, fifteen, transport2, five);

		// Setting up 2nd journey
		transport1 = new Transport(ModeTransport.METRO, five, "15", "A");
		transport2 = new Transport(ModeTransport.BUS, fifteen, "12", "D"); // different
		Journey wontBeAffected = setup2sectionJourney(transport1, five, transport2, fifteen);

		// Grouping things
		List<Journey> availableJourneys = new ArrayList<>();
		availableJourneys.add(willBeAffected);
		availableJourneys.add(wontBeAffected);

		List<IncidentTransport> currentDelays = new ArrayList<>();
		List<IncidentWeather> currentWeathers = new ArrayList<>();
		currentDelays.add(delay);

		// Actual testing
		Journey selectedJourney = engine.selectJourney(0, failedJourney, availableJourneys, currentDelays,
				currentWeathers, List.of(), false);
		assertFalse(selectedJourney.isAffectedBy(delay));
	}

	@Test
	void greenModeTest() {
		MockAPI mockapi = new MockAPI();
		String input1 = mockapi.loadResource("demo1/fnor.json");
		String input2 = mockapi.loadResource("demo1/fridf.json");
		String input3 = mockapi.loadResource("demo1/frne.json");
		String input4 = mockapi.loadResource("demo1/frnw.json");
		String input5 = mockapi.loadResource("demo1/none.json");

		JourneyService journeyService = new JourneyService();
		List<Journey> journeys =new ArrayList<Journey>(); 
		journeys.addAll(journeyService.jsonJourneyToObject(input1));
		journeys.addAll(journeyService.jsonJourneyToObject(input2));
		journeys.addAll(journeyService.jsonJourneyToObject(input3));
		journeys.addAll(journeyService.jsonJourneyToObject(input4));
		journeys.addAll(journeyService.jsonJourneyToObject(input5));
		// mode green sans filtre
		assertDoesNotThrow(() -> engine.selectJourney(0, null, journeys, List.of(), List.of(), List.of(), true));
		Journey select= engine.selectJourney(0, null, journeys, List.of(), List.of(), List.of(), true);
		assertTrue(journeys.get(3)==select);
		//mode normal sans filtre
		select= engine.selectJourney(0, null, journeys, List.of(), List.of(), List.of(), false);
		assertTrue(journeys.get(5)==select);
		//mode green avec filtre
		List<Prohibition> l= new ArrayList<Prohibition>();
		l.add(new Prohibition( ModeTransport.METRO));
		select= engine.selectJourney(0, null, journeys, List.of(), List.of(), l, true);
		assertTrue(journeys.get(6)==select);
		
		
	}

	@Test
	void testSelectedJourneyWhenAllNullOrEmpty() {
		assertDoesNotThrow(() -> engine.selectJourney(0, null, List.of(), List.of(), List.of(), List.of(), false));
	}
}