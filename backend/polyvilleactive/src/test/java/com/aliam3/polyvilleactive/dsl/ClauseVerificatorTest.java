package com.aliam3.polyvilleactive.dsl;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aliam3.polyvilleactive.exception.SemanticException;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;



public class ClauseVerificatorTest {

	ClauseVerificator cv;
	
	@BeforeEach
	void setUp() {
		cv= new ClauseVerificator();
	}
	@Test
	void prioriteTest() {
		assertDoesNotThrow(() -> cv.priorite("PRIORISER", ModeTransport.BUS, 1));
		assertDoesNotThrow(() -> cv.priorite("EVITER", ModeTransport.BUS, -1));
		assertDoesNotThrow(() -> cv.priorite("EVITER", ModeTransport.BUS, -8));
		assertDoesNotThrow(() -> cv.priorite("EVITER", ModeTransport.BUS, 3));
		assertThrows(SemanticException.class,() -> cv.priorite("PRIORISER", ModeTransport.BUS, -1));
		assertThrows(SemanticException.class,() -> cv.priorite("PRIORISER", ModeTransport.BUS, 2));
		assertEquals(1,cv.getPrioritize().size(), "taille 1");
		assertEquals(3,cv.getAvoid().size(), "taille 3");
    }
	
	@Test
	void verifyTest() {
		cv.priorite("PRIORISER", ModeTransport.BUS, 1);
		cv.priorite("PRIORISER", ModeTransport.TRAMWAY, 1);
		cv.priorite("EVITER", ModeTransport.TRAMWAY, -1);
		assertThrows(SemanticException.class,() -> cv.verify());
		cv.resetList();
		cv.priorite("PRIORISER", ModeTransport.TRAMWAY, 1);
		cv.interdire(ModeTransport.TRAMWAY);
		assertThrows(SemanticException.class,() -> cv.verify());
		cv.resetList();
		cv.priorite("EVITER", ModeTransport.TRAMWAY, 1);
		cv.interdire(ModeTransport.TRAMWAY);
		assertThrows(SemanticException.class,() -> cv.verify());
		cv.resetList();
		cv.priorite("PRIORISER", ModeTransport.TRAMWAY, 1);
		cv.priorite("PRIORISER", ModeTransport.BUS, 1);
		assertThrows(SemanticException.class,() -> cv.verify());
		cv.resetList();
		cv.interdire(ModeTransport.BUS);
		cv.priorite("EVITER", ModeTransport.BUS, 1);
		assertThrows(SemanticException.class,() -> cv.verify());
		cv.resetList();
		cv.interdire(ModeTransport.BUS);
		cv.priorite("PRIORISER", ModeTransport.BUS, 1);
		assertThrows(SemanticException.class,() -> cv.verify());
		cv.resetList();
		cv.priorite("PRIORISER", ModeTransport.TRAMWAY, 1);
		cv.priorite("PRIORISER", ModeTransport.BUS, 2);
		cv.priorite("EVITER", ModeTransport.BIKE, -1);
		cv.interdire(ModeTransport.METRO);
		assertDoesNotThrow(() -> cv.verify());
		
	}
}
