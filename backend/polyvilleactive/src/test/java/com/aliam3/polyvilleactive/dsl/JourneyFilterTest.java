package com.aliam3.polyvilleactive.dsl;

import com.aliam3.polyvilleactive.model.transport.Journey;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JourneyFilterTest {

    private List<Priorite> priorities;
    private List<Prohibition> prohibitions;
    private List<Journey> journeys;
    private JourneyFilter filter;

    @BeforeEach
    void setUp() {
        priorities = new ArrayList<>();
        prohibitions = new ArrayList<>();
        journeys = new ArrayList<>();
    }
    @Test
    void testPrioritiesAreRespected() {
        Map<ModeTransport,Long> mostlyWalkingMap = new HashMap<>();
        mostlyWalkingMap.put(ModeTransport.BUS, 200L);
        mostlyWalkingMap.put(ModeTransport.WALKING, 10L);
        Journey mostlyBus = mock(Journey.class);
        when(mostlyBus.getTransports()).thenReturn(mostlyWalkingMap);

        Map<ModeTransport,Long> mostlySubwayMap = new HashMap<>();
        mostlySubwayMap.put(ModeTransport.BUS, 5L);
        mostlySubwayMap.put(ModeTransport.WALKING, 1000L);
        Journey mostlyWalking = mock(Journey.class);
        when(mostlyWalking.getTransports()).thenReturn(mostlySubwayMap);


        priorities.add(new Priorite(1,ModeTransport.WALKING));
        filter = new JourneyFilter(priorities,prohibitions);

        journeys.add(mostlyBus);
        journeys.add(mostlyWalking);

        List<Journey> res = filter.applyPreferences(journeys);
        assertEquals(mostlyWalking, res.get(0));
    }

    @Test
    void testAvoidancesAreRespected() {
        Map<ModeTransport,Long> mostlyWalkingMap = new HashMap<>();
        mostlyWalkingMap.put(ModeTransport.BUS, 200L);
        mostlyWalkingMap.put(ModeTransport.METRO, 100L);
        Journey lessMetro = mock(Journey.class);
        when(lessMetro.getTransports()).thenReturn(mostlyWalkingMap);

        Map<ModeTransport,Long> mostlySubwayMap = new HashMap<>();
        mostlySubwayMap.put(ModeTransport.BUS, 100L);
        mostlySubwayMap.put(ModeTransport.METRO, 200L);
        Journey moreMetro = mock(Journey.class);
        when(moreMetro.getTransports()).thenReturn(mostlySubwayMap);

        priorities.add(new Priorite(-1,ModeTransport.METRO));
        filter = new JourneyFilter(priorities,List.of());

        journeys.add(moreMetro);
        journeys.add(lessMetro);

        List<Journey> res = filter.applyPreferences(journeys);
        Journey last = res.get(1);
        assertEquals(moreMetro, last);
    }

    @Test
    void testApplyProhibitions() {
        Map<ModeTransport,Long> usingBus = new HashMap<>();
        usingBus.put(ModeTransport.BUS, 200L);
        usingBus.put(ModeTransport.METRO, 100L);
        Journey useBus = mock(Journey.class);
        when(useBus.getTransports()).thenReturn(usingBus);
        journeys.add(useBus);

        Map<ModeTransport,Long> usingNoBus = new HashMap<>();
        usingNoBus.put(ModeTransport.TRAMWAY, 100L);
        usingNoBus.put(ModeTransport.METRO, 200L);
        Journey useNoBus = mock(Journey.class);
        when(useNoBus.getTransports()).thenReturn(usingNoBus);
        journeys.add(useNoBus);

        prohibitions.add(new Prohibition(ModeTransport.BUS));
        filter = new JourneyFilter(List.of(),prohibitions);
        List<Journey> res = filter.applyProhibitions(journeys);

        assertFalse(res.contains(useBus));
        assertTrue(res.contains(useNoBus));
    }
}