package com.aliam3.polyvilleactive.model;
import com.aliam3.polyvilleactive.db.MockAPI;
import com.aliam3.polyvilleactive.model.incidents.transportation.Delay;
import com.aliam3.polyvilleactive.model.transport.Journey;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.service.JourneyService;
import org.junit.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JourneyTest {

    @Test
    public void isAffectedByTest(){

        MockAPI mockAPI = new MockAPI();
        JourneyService journeyService = new JourneyService();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test2.json"));

        Delay delay1= new Delay(Duration.ofSeconds(2000), "Pontoise / Versailles R. Gauche / St-Quentin en Y. - Versailles Ch. / Dourdan la F. / St-Martin d'E", ModeTransport.TRAIN);
        assertTrue(journeys.get(0).isAffectedBy(delay1));
        Delay delay2= new Delay(Duration.ofSeconds(2000), "Pontoise / Versailles R. Gauche / St-Quentin en Y. - Versailles Ch. / Dourdan la F. / St-Martin d'E", ModeTransport.METRO);

        assertFalse(journeys.get(0).isAffectedBy(delay2));

    }
    
    @Test
    public void getCurrentStep() {
    	 MockAPI mockAPI = new MockAPI();
         JourneyService journeyService = new JourneyService();
         List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test2.json"));
         assertEquals(journeys.get(0).getSections().get(0), journeys.get(0).getCurrentStep());
         journeys.get(0).getSections().get(0).setReached(true);
         journeys.get(0).getSections().get(2).setReached(true);
         journeys.get(0).getSections().get(3).setReached(true);
         assertEquals(journeys.get(0).getSections().get(1), journeys.get(0).getCurrentStep());
         journeys.get(0).getSections().get(1).setReached(true);
         assertEquals(journeys.get(0).getSections().get(4), journeys.get(0).getCurrentStep());
    	
    }
}
