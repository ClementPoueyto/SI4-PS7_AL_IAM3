package com.aliam3.polyvilleactive.model;

import com.aliam3.polyvilleactive.db.MockAPI;
import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.incidents.transportation.Delay;
import com.aliam3.polyvilleactive.model.incidents.transportation.FullPeople;
import com.aliam3.polyvilleactive.model.incidents.transportation.IncidentTransport;
import com.aliam3.polyvilleactive.model.transport.Journey;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.service.JourneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IncidentTest {

    private Incident incident;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void affectsJourneyTest(){
        MockAPI mockAPI = new MockAPI();
        JourneyService journeyService = new JourneyService();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test2.json"));
        Journey journey = journeys.get(0);
        incident = new Delay(Duration.ofSeconds(100), "test", ModeTransport.BUS);
        assertFalse(incident.affects(journey));
        incident = new Delay(Duration.ofSeconds(100), "Cergy Le Haut / Poissy / St-Germain-en-Laye - Marne-la-Vallée Chessy Disneyland / Boissy-St-Léger", ModeTransport.TRAIN);
        assertTrue(incident.affects(journey));


        journey.getSections().get(0).setReached(true);
        incident = new Delay(Duration.ofSeconds(100), "Cergy Le Haut / Poissy / St-Germain-en-Laye - Marne-la-Vallée Chessy Disneyland / Boissy-St-Léger", ModeTransport.TRAIN);
        assertTrue(incident.affects(journey));

        journey.getSections().get(1).setReached(true);
        incident = new Delay(Duration.ofSeconds(100), "Cergy Le Haut / Poissy / St-Germain-en-Laye - Marne-la-Vallée Chessy Disneyland / Boissy-St-Léger", ModeTransport.TRAIN);
        assertFalse(incident.affects(journey));

    }

    @Test
    public void affectsJourneyTest2(){
        MockAPI mockAPI = new MockAPI();
        JourneyService journeyService = new JourneyService();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test2.json"));
        Journey journey = journeys.get(0);
        incident = new FullPeople("test", ModeTransport.BUS);
        assertFalse(incident.affects(journey));
        incident = new FullPeople( "Cergy Le Haut / Poissy / St-Germain-en-Laye - Marne-la-Vallée Chessy Disneyland / Boissy-St-Léger", ModeTransport.TRAIN);
        assertTrue(incident.affects(journey));

        journey.getSections().get(0).setReached(true);
        incident = new FullPeople("Cergy Le Haut / Poissy / St-Germain-en-Laye - Marne-la-Vallée Chessy Disneyland / Boissy-St-Léger", ModeTransport.TRAIN);
        assertFalse(incident.affects(journey));

        journey.getSections().get(1).setReached(true);
        incident = new FullPeople("Cergy Le Haut / Poissy / St-Germain-en-Laye - Marne-la-Vallée Chessy Disneyland / Boissy-St-Léger", ModeTransport.TRAIN);
        assertFalse(incident.affects(journey));

    }

}
