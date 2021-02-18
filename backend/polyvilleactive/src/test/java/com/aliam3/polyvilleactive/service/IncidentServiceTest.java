package com.aliam3.polyvilleactive.service;

import com.aliam3.polyvilleactive.db.MockAPI;
import com.aliam3.polyvilleactive.model.gamification.Demands;
import com.aliam3.polyvilleactive.model.gamification.ResponseDemands;
import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.incidents.transportation.Delay;
import com.aliam3.polyvilleactive.model.incidents.transportation.FullPeople;
import com.aliam3.polyvilleactive.model.transport.Journey;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.model.transport.Section;
import com.aliam3.polyvilleactive.model.transport.Transport;
import com.aliam3.polyvilleactive.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration
@SpringBootTest
public class IncidentServiceTest {

    @Autowired
    JourneyService journeyService;

    @Autowired
    IncidentService incidentService;

    @Autowired
    UserService userService;

    @BeforeEach
    public void setUp() {
        incidentService.incidents=new ArrayList<>();
        journeyService.demandsList=new ArrayList<>();
        journeyService.journeyList=new ArrayList<>();

        userService.users = new ArrayList<>();
        userService.updateUser(new User(1,"user1","pwd",""));
        userService.updateUser(new User(10,"user10","pwd",""));
    }

    @Test
    public void updateListTest(){

        Delay test1= new Delay(Duration.ofSeconds(1000), "A", ModeTransport.TRAIN);
        Delay test2= new Delay(Duration.ofSeconds(1000),  "C", ModeTransport.METRO);
        Delay test3= new Delay(Duration.ofSeconds(3000), "C", ModeTransport.METRO);


        incidentService.incidents.add(test1);
        incidentService.incidents.add(test2);

        incidentService.updateList(test3);
        assertEquals(Duration.ofSeconds(3000), incidentService.incidents.get(1).getTime());
    }

    @Test
    public void lookImpactTest(){
        long id = 1;
        MockAPI mockAPI = new MockAPI();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json"));
        journeys.get(0).setIdUser(id);
        journeyService.journeyList = journeys;
        incidentService.incidents=new ArrayList<>();
        assertEquals(0,incidentService.lookImpact(id).size());
        incidentService.incidents.add(new Delay(Duration.ofSeconds(2000),"C", ModeTransport.METRO));
        assertEquals(0,incidentService.lookImpact(id).size());
        incidentService.updateList((new Delay(Duration.ofSeconds(2000),"Pontoise / Versailles R. Gauche / St-Quentin en Y. - Versailles Ch. / Dourdan la F. / St-Martin d'E", ModeTransport.TRAIN)));
        assertEquals(1,incidentService.lookImpact(id).size());

    }

    @Test
    public void testLookImpactCreatesDemandWhenFull() {
        MockAPI mockAPI = new MockAPI();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json"));
        Journey journey0 = journeys.get(0);
        journey0.setIdUser(1L);

        Transport transport = new Transport(ModeTransport.BUS,Duration.ofMinutes(5),"10","A");
        FullPeople fullPeople = new FullPeople("A",ModeTransport.BUS);
        Demands expectedDemand = new Demands(1L,transport);

        Section section0 = journey0.getSections().get(0);
        section0.setTransport( transport );

        assertTrue(fullPeople.affects(section0));
        journeyService.updateList(journey0);
        incidentService.updateList(fullPeople);


        List<Incident> reportedIncidents = incidentService.lookImpact(1L);

        assertEquals(1,journeyService.demandsList.size(),"One demand was created");
        Demands createdDemand = journeyService.demandsList.get(0);
        assertAll("This demand is as expected",
                () -> assertEquals(expectedDemand.getIdUserAsking(),createdDemand.getIdUserAsking()),
                () -> assertEquals(expectedDemand.getTransport(),createdDemand.getTransport())
        );
        assertEquals(0,reportedIncidents.size(),"The incident isn't reported");
    }

    @Test
    public void testLookImpactReportsFullWhenDemandWasAccepted() {
        MockAPI mockAPI = new MockAPI();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json"));
        Journey journey0 = journeys.get(0);
        journey0.setIdUser(1L);

        Transport transport = new Transport(ModeTransport.BUS,Duration.ofMinutes(5),"10","A");
        FullPeople fullPeople = new FullPeople("A",ModeTransport.BUS);
        Demands existingDemand = new Demands(1L,transport);

        Section section0 = journey0.getSections().get(0);
        section0.setTransport( transport );

        assertTrue(fullPeople.affects(section0));
        journeyService.updateList(journey0);
        incidentService.updateList(fullPeople);

        journeyService.addDemands(existingDemand);
        journeyService.saveResponseDemands(new ResponseDemands(existingDemand.getId(),true),10L); // User10 needs to exist
        assertTrue(existingDemand.wasAccepted(),"The demand is accepted");


        List<Incident> reportedIncidents = incidentService.lookImpact(1L);
        assertEquals(1,journeyService.demandsList.size(),"Demand is still there");
        assertEquals(0,reportedIncidents.size(),"The incident isn't reported");
    }

    @Test
    public void testLookImpactReportsFullWhenDemandExpiredAndNotAccepted() {
        MockAPI mockAPI = new MockAPI();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json"));
        Journey journey0 = journeys.get(0);
        journey0.setIdUser(1L);

        Transport transport = new Transport(ModeTransport.BUS,Duration.ofMinutes(5),"10","A");
        FullPeople fullPeople = new FullPeople("A",ModeTransport.BUS);
        Demands existingDemand = new Demands(1L,transport);

        Section section0 = journey0.getSections().get(0);
        section0.setTransport( transport );

        assertTrue(fullPeople.affects(section0));
        journeyService.updateList(journey0);
        incidentService.updateList(fullPeople);

        journeyService.addDemands(existingDemand);
        journeyService.saveResponseDemands(new ResponseDemands(existingDemand.getId(),false),10L); // User10 created in setup
        existingDemand.end();
        assertTrue(existingDemand.hasEnded(),"The demand expired");


        List<Incident> reportedIncidents = incidentService.lookImpact(1L);
        assertEquals(1,journeyService.demandsList.size(),"Demand is still there");
        assertEquals(1,reportedIncidents.size(),"The incident isn't reported");
    }
}
