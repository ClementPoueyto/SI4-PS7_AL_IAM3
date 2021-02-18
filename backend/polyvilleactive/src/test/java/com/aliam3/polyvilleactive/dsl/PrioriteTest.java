package com.aliam3.polyvilleactive.dsl;

import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrioriteTest {

    @Test
    void testSameObjectsEqual() {
        Priorite priorite1 = new Priorite(1, ModeTransport.BUS);
        Priorite priorite1bis = new Priorite(1, ModeTransport.BUS);
        assertTrue(priorite1.equals(priorite1bis));
    }

    @Test
    void testDifferentObjectsDoNotEqual() {
        Priorite priorite1 = new Priorite(1, ModeTransport.BUS);
        Priorite priorite1bis = new Priorite(1, ModeTransport.METRO);
        assertFalse(priorite1.equals(priorite1bis));
    }

    @Test
    void testSameObjectSameHashCode() {
        Priorite priorite1 = new Priorite(1, ModeTransport.BUS);
        Priorite priorite1bis = new Priorite(1, ModeTransport.BUS);
        assertEquals(priorite1.hashCode(),priorite1bis.hashCode());
    }

    @Test
    void testCompromiseAvoidAndPriorityCancelEachOther() {
        List<Priorite> allPriorities = new ArrayList<>();
        allPriorities.add(new Priorite(2,ModeTransport.BUS));
        allPriorities.add(new Priorite(-1,ModeTransport.BUS));
        List<Priorite> res = Priorite.compromise(allPriorities);
        assertTrue(res.isEmpty());
    }

    @Test
    void testCompromiseMeanBetweenPriorities() {
        List<Priorite> allPriorities = new ArrayList<>();
        allPriorities.add(new Priorite(2,ModeTransport.BUS));
        allPriorities.add(new Priorite(4,ModeTransport.BUS));
        List<Priorite> res = Priorite.compromise(allPriorities);
        assertEquals(1,res.size());
        assertEquals(new Priorite(3,ModeTransport.BUS),res.get(0));
    }

    @Test
    void testCompromiseAvoidWhenOnlyAvoid() {
        List<Priorite> allPriorities = new ArrayList<>();
        allPriorities.add(new Priorite(-1,ModeTransport.BUS));
        allPriorities.add(new Priorite(-1,ModeTransport.BUS));
        List<Priorite> res = Priorite.compromise(allPriorities);
        assertEquals(1,res.size());
        assertEquals(new Priorite(-1,ModeTransport.BUS),res.get(0));
    }
}