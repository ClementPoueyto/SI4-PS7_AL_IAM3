package com.aliam3.polyvilleactive.model;


import com.aliam3.polyvilleactive.db.MockAPI;
import com.aliam3.polyvilleactive.model.incidents.transportation.Delay;
import com.aliam3.polyvilleactive.model.transport.Journey;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.service.JourneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DelayTest {

    private Delay delay;

    @BeforeEach
    void setUp() {
        delay= new Delay(Duration.ZERO,"Pontoise / Versailles R. Gauche / St-Quentin en Y. - Versailles Ch. / Dourdan la F. / St-Martin d'E", ModeTransport.METRO);
    }


    @Test
    public void isNotPassedTest() {
        delay.setTime(Duration.ofMinutes(100));
        assertFalse(delay.isPassed());
    }

    @Test
    public void isPassedTest() throws InterruptedException {
        delay.setTime(Duration.ofMillis(1));
        Thread.sleep(1000);
        assertTrue(delay.isPassed());
    }

    @Test
    public void affectsTest(){
        MockAPI mockAPI = new MockAPI();
        JourneyService journeyService = new JourneyService();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test2.json"));

        delay= new Delay(Duration.ofSeconds(2000), "Pontoise / Versailles R. Gauche / St-Quentin en Y. - Versailles Ch. / Dourdan la F. / St-Martin d'E", ModeTransport.TRAIN);
        assertTrue(delay.affects(journeys.get(0)));
        delay= new Delay(Duration.ofSeconds(2000), "Lontoise / Versailles R. Gauche / St-Quentin en Y. - Versailles Ch. / Dourdan la F. / St-Martin d'E", ModeTransport.TRAIN);
        assertFalse(delay.affects(journeys.get(0)));
        delay= new Delay(Duration.ofSeconds(2000), "Pontoise / Versailles R. Gauche / St-Quentin en Y. - Versailles Ch. / Dourdan la F. / St-Martin d'E", ModeTransport.METRO);
        assertFalse(delay.affects(journeys.get(0)));
    }
}
