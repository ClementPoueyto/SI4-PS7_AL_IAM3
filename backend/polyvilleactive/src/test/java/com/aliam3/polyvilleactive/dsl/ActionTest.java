package com.aliam3.polyvilleactive.dsl;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ActionTest {

    @Test
    void testSameObjectsEqual() {
        List<Priorite> prios = List.of(mock(Priorite.class),mock(Priorite.class));
        List<Priorite> prios1 = new ArrayList<>(prios);
        List<Priorite> prios1bis = new ArrayList<>(prios);

        Action a1 = new Action(prios1, new ArrayList<>());
        Action a2 = new Action(prios1bis, new ArrayList<>());

        assertTrue(a1.equals(a2));
    }

    @Test
    void testDifferentObjectsDoNotEqual() {
        List<Priorite> prios1 = new ArrayList<>(List.of(mock(Priorite.class)));
        List<Priorite> prios1bis = new ArrayList<>(List.of(mock(Priorite.class)));

        Action a1 = new Action(prios1, new ArrayList<>());
        Action a2 = new Action(prios1bis, new ArrayList<>());

        assertFalse(a1.equals(a2));
    }

    @Test
    void testSameObjectSameHashCode() {
        List<Priorite> prios = List.of(mock(Priorite.class),mock(Priorite.class));
        List<Priorite> prios1 = new ArrayList<>(prios);
        List<Priorite> prios1bis = new ArrayList<>(prios);

        Action a1 = new Action(prios1, new ArrayList<>());
        Action a2 = new Action(prios1bis, new ArrayList<>());

        assertEquals(a1.hashCode(),a2.hashCode());
    }
}