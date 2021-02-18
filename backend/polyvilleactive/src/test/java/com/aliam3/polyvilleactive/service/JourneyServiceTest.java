package com.aliam3.polyvilleactive.service;

import com.aliam3.polyvilleactive.db.MockAPI;
import com.aliam3.polyvilleactive.model.gamification.Demands;
import com.aliam3.polyvilleactive.model.gamification.ResponseDemands;
import com.aliam3.polyvilleactive.model.location.Position;
import com.aliam3.polyvilleactive.model.location.StepState;
import com.aliam3.polyvilleactive.model.transport.Journey;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.model.transport.Section;
import com.aliam3.polyvilleactive.model.transport.Transport;
import com.aliam3.polyvilleactive.model.user.Form;
import com.aliam3.polyvilleactive.model.user.User;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JourneyServiceTest {

    MockAPI mockapi;

    @Autowired
    JourneyService journeyService;

    @Autowired
    UserService userService;

    @BeforeEach
    public void setUp()  {
        mockapi= new MockAPI();
        journeyService.journeyList = new ArrayList<>();

    }


    @Test
    public void jsonToObjectTest() throws IOException {
    	
        String input = mockapi.loadResource("mock/test2.json");

        journeyService = new JourneyService();
       List<Journey> journeys = journeyService
               .jsonJourneyToObject(input);
       assertEquals(1,journeys.size());

       assertEquals("Avenue Paul Séramy (Chessy)",journeys.get(0).getDeparture().getAdress());
       assertEquals("51 Rue du Général Leclerc (Issy-les-Moulineaux)",journeys.get(0).getArrival().getAdress());

        assertEquals(8,journeys.get(0).getSections().size());
        assertEquals("MARNE LA VALLEE CHESSY (Chessy)",journeys.get(0).getSections().get(0).getTo().getAdress());

        assertEquals(ModeTransport.TRAIN,journeys.get(0).getSections().get(6).getTransport().getModeTransport());

        LocalDateTime arrive=null;

        for(Section sect :journeys.get(0).getSections()){

            if(arrive!=null){
                assertEquals(sect.getDepartureDateTime(),arrive);
            }

            arrive = sect.getArrivalDateTime();
            assertNotNull(sect.getTransport().getModeTransport());
            assertNotNull(sect.getArrivalDateTime());

        }
    }

    @Test
    public void jsonToObjectTest1() throws IOException {
        String input = mockapi.loadResource("mock/test1.json");

        journeyService = new JourneyService();
        List<Journey> journeys = journeyService
                .jsonJourneyToObject(input);
        assertEquals(1,journeys.size());

        assertEquals("5 Avenue Anatole France (Paris)",journeys.get(0).getDeparture().getAdress());
        assertEquals("Cour des Cerfs (Versailles)",journeys.get(0).getArrival().getAdress());

        assertEquals(3,journeys.get(0).getSections().size());
        assertEquals("CHAMP DE MARS TOUR EIFFEL (Paris)",journeys.get(0).getSections().get(0).getTo().getAdress());

        assertEquals(ModeTransport.WALKING,journeys.get(0).getSections().get(2).getTransport().getModeTransport());

        LocalDateTime arrive=null;

        for(Section sect :journeys.get(0).getSections()){

            if(arrive!=null){
                assertEquals(sect.getDepartureDateTime(),arrive);
            }

            arrive = sect.getArrivalDateTime();

            assertNotNull(sect.getTransport().getModeTransport());
            assertNotNull(sect.getArrivalDateTime());

        }


    }

    @Test
    public void updateListTest(){
        journeyService.journeyList = new ArrayList<>();
        Journey test1= mock(Journey.class);
        when(test1.getIdUser()).thenReturn(1L);
        Journey test2= mock(Journey.class);
        when(test2.getIdUser()).thenReturn(1L);
        Journey test3= mock(Journey.class);
        when(test3.getIdUser()).thenReturn(1L);

        journeyService.journeyList.add(test1);
        journeyService.journeyList.add(test2);

        journeyService.updateList(test3);
        assertEquals(1, journeyService.journeyList.size());
    }

    @Test
    public void testValidateJourneyStepTargetsCorrectSection() {
        // SETUP
        journeyService.journeyList = new ArrayList<>();

        MockAPI mockAPI = new MockAPI();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json"));
        Journey journey1 = journeys.get(0);
        journey1.setIdUser(1L);

        journeyService.journeyList.add(journey1);

        // TEST
        journeyService.validateJourneyStep(1L,new Position(48.855909,2.28968), new Form());
        assertTrue(journey1.getSections().get(0).isReached());
        assertFalse(journey1.getSections().get(1).isReached());
    }

    @Test
    public void testValidateJourneyStepWhenNoSectionMatchesPosition() {
        // SETUP
        journeyService.journeyList = new ArrayList<>();

        MockAPI mockAPI = new MockAPI();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json"));
        Journey journey1 = journeys.get(0);
        journey1.setIdUser(1L);

        journeyService.journeyList.add(journey1);

        // TEST
        assertEquals(StepState.ERROR, journeyService.validateJourneyStep(1L,new Position(0,0), new Form()));
        assertFalse(journey1.getSections().get(0).isReached());
        assertFalse(journey1.getSections().get(1).isReached());
    }

    @Test
    public void testValidateJourneyStepWhenUserHasNoJourney() {
        /// SETUP
        journeyService.journeyList = new ArrayList<>();

        MockAPI mockAPI = new MockAPI();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json"));
        Journey journey1 = journeys.get(0);
        journey1.setIdUser(1L);

        journeyService.journeyList.add(journey1);

        // TEST
        assertThrows(IllegalArgumentException.class, ()->journeyService.validateJourneyStep(42L,new Position(48.855909,2.28968), new Form()));
        assertFalse(journey1.getSections().get(0).isReached());
        assertFalse(journey1.getSections().get(1).isReached());
    }

    @Test
    public void terminateJourneyTest(){
        /// SETUP
        User user = new User();
        userService.users.add(user);
        journeyService.journeyList = new ArrayList<>();

        MockAPI mockAPI = new MockAPI();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json"));
        Journey journey1 = journeys.get(0);
        journey1.setIdUser(user.getId());
        journeyService.journeyList.add(journey1);
        Form form = new Form();

        assertEquals(1, journeyService.journeyList.size());
        assertEquals(0, userService.searchForUser(user.getId()).get().getScore());
        assertEquals(0, userService.searchForUser(user.getId()).get().getPoints());
        journeyService.terminateJourney(journey1, form);
        assertEquals(0, journeyService.journeyList.size());
        assertEquals(25, userService.searchForUser(user.getId()).get().getScore());
        assertEquals(25, userService.searchForUser(user.getId()).get().getPoints());
        assertTrue(userService.searchForUser(user.getId()).get().getBadges().size()>0);

    }

    @Test
    public void isJourneyOverTest(){
        journeyService.journeyList = new ArrayList<>();

        MockAPI mockAPI = new MockAPI();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json"));
        Journey journey = journeys.get(0);

        assertFalse(journeyService.isJourneyOver(journey, journey.getSections().get(1)));
        assertFalse(journeyService.isJourneyOver(journey, journey.getSections().get(journey.getSections().size()-2)));
        assertTrue(journeyService.isJourneyOver(journey, journey.getSections().get(journey.getSections().size()-1)));

    }

    @Test
    void testLookForDemandsWhenThereIsOne() {
        // Retrieve real journey
        MockAPI mockAPI = new MockAPI();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json"));

        // Change user id in journey
        Journey journey1 = journeys.get(0);
        journey1.setIdUser(1L);

        // Setup some transport
        Transport transport1 = new Transport(ModeTransport.METRO, Duration.ofMinutes(10), "12", "A");
        Transport transport1bis = new Transport(ModeTransport.METRO, Duration.ofMinutes(5), "12", "A");
        Transport anotherTransport = new Transport(ModeTransport.BUS, Duration.ofMinutes(10), "2", "Y");

        // Change transport in first section of journey
        Section section1 = journey1.getSections().get(0);
        section1.setTransport(transport1);

        journeyService.journeyList.clear();
        journeyService.journeyList.add(journey1);

        Demands matchingDemand = new Demands(1L,transport1bis);
        Demands nonMatchingDemand = new Demands(1L,anotherTransport);

        journeyService.demandsList.clear();
        journeyService.demandsList.add(matchingDemand);
        journeyService.demandsList.add(nonMatchingDemand);

        Demands result = journeyService.lookForDemands(1L);
        assertEquals(matchingDemand,result);
    }

    @Test
    void testLookForDemandsWhenProviderHasNoJourney() {
        // Retrieve real journey
        MockAPI mockAPI = new MockAPI();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json"));

        // Change user id in journey
        Journey journey1 = journeys.get(0);
        journey1.setIdUser(8L); // Different user

        // Setup some transport
        Transport transport1 = new Transport(ModeTransport.METRO, Duration.ofMinutes(10), "12", "A");
        Transport transport1bis = new Transport(ModeTransport.METRO, Duration.ofMinutes(5), "12", "A");
        Transport anotherTransport = new Transport(ModeTransport.BUS, Duration.ofMinutes(10), "2", "Y");

        // Change transport in first section of journey
        Section section1 = journey1.getSections().get(0);
        section1.setTransport(transport1);

        journeyService.journeyList.clear();
        journeyService.journeyList.add(journey1);

        Demands matchingDemand = new Demands(1L,transport1bis); // Different user
        Demands nonMatchingDemand = new Demands(1L,anotherTransport); // Different user

        journeyService.demandsList.clear();
        journeyService.demandsList.add(matchingDemand);
        journeyService.demandsList.add(nonMatchingDemand);

        Demands result = journeyService.lookForDemands(1L);
        assertNull(result);
    }

    @Test
    void testLookForDemandsWhenNoMatchedDemand() {
        // Retrieve real journey
        MockAPI mockAPI = new MockAPI();
        List<Journey> journeys = journeyService.jsonJourneyToObject(mockAPI.loadResource("mock/test1.json"));

        // Change user id in journey
        Journey journey1 = journeys.get(0);
        journey1.setIdUser(1L); // Same user

        // Setup some transport
        Transport transport1 = new Transport(ModeTransport.METRO, Duration.ofMinutes(10), "12", "A");
        Transport transport2 = new Transport(ModeTransport.TRAMWAY, Duration.ofMinutes(5), "4", "C");
        Transport transport3 = new Transport(ModeTransport.BUS, Duration.ofMinutes(10), "2", "Y");

        // Change transport in first section of journey
        Section section1 = journey1.getSections().get(0);
        section1.setTransport(transport1); // Section has only WALKING and METRO

        journeyService.journeyList.clear();
        journeyService.journeyList.add(journey1);

        Demands nonMatchingDemand1 = new Demands(1L,transport2); // Same user different transport
        Demands nonMatchingDemand2 = new Demands(1L,transport3); // Same user different transport again

        journeyService.demandsList.clear();
        journeyService.demandsList.add(nonMatchingDemand1);
        journeyService.demandsList.add(nonMatchingDemand2);

        Demands result = journeyService.lookForDemands(1L);
        assertNull(result);
    }
    
    @Test
    public void saveResponseDemand() {
    	Demands d =new Demands(0,new Transport(ModeTransport.METRO, Duration.ofMinutes(5)));
    	journeyService.addDemands(d);
    	ResponseDemands r= new ResponseDemands(2, true);
    	// pas la meme demande
    	assertEquals(0, journeyService.saveResponseDemands(r, 1));
    	r= new ResponseDemands(d.getId(), true);
    	assertEquals(1, journeyService.saveResponseDemands(r, 2));
    	r= new ResponseDemands(d.getId(), false);
    	assertEquals(2, journeyService.saveResponseDemands(r, 3));
    	assertEquals(2, journeyService.saveResponseDemands(r, 2));
    	d.end();
    	journeyService.demandsList.clear();
    	journeyService.addDemands(d);
    	assertEquals(3, journeyService.saveResponseDemands(r, 2));
    }

}
