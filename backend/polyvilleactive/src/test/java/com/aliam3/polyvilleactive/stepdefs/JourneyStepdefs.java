package com.aliam3.polyvilleactive.stepdefs;


import com.aliam3.polyvilleactive.MemoryRule;
import com.aliam3.polyvilleactive.model.location.StepState;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.model.user.Form;
import com.aliam3.polyvilleactive.service.IncidentService;
import com.aliam3.polyvilleactive.service.JourneyService;
import io.cucumber.java8.Fr;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(value = Cucumber.class)
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@CucumberOptions(plugin = {"pretty"}, features = "src/test/resources/features")
public class JourneyStepdefs implements Fr {

    @Autowired
    JourneyService journeyService;

    @Autowired
    IncidentService incidentService;
    @Autowired
    private MockMvc mvc;
    MvcResult result;
    Form form;
    Double latFrom;
    Double longFrom;
    Double latTo;
    Double longTo;
    List<String> filters = new ArrayList<>();
    List<ModeTransport> filtersTransports = new ArrayList<>();
    String myFilter;
    int idUser;
    public JourneyStepdefs() {


        Etantdonné("Un utilisateur connecté à l'application mobile avec un id égal à {int}",
                (Integer id) -> {
            incidentService.incidents = new ArrayList<>();
            journeyService.journeyList=new ArrayList<>();
            MemoryRule.GLOBALPRIORITIZE= new ArrayList<>();
            MemoryRule.RULES= new ArrayList<>();
            filters=new ArrayList<>();
            filtersTransports = new ArrayList<>();
            idUser=id;
            myFilter="";
        });
        Et("il rentre un depart lat : {double}, longitude {double} et une arrivee lat : {double}, longitude {double}",(Double latStart, Double longStart, Double latArrival, Double longArrival)->{
            latFrom=latStart;
            longFrom=longStart;

            latTo=latArrival;
            longTo=longArrival;
        });

        Et("il filtre le transport {string}",(String myfilter)->{
            filters.add(myfilter);
            filtersTransports.add(ModeTransport.valueOf(myfilter));
        });
        Quand("il envoie la requete", () -> {
            myFilter = "";
            for (String transport: filters
                 ) {
                myFilter+="\""+transport.toUpperCase()+"\",";

            }
            if ((myFilter != null) && (myFilter.length() > 0)) {
                myFilter = myFilter.substring(0, myFilter.length() - 1);
            }
            form = new Form(filtersTransports, false);
            result = mvc.perform(post("/journey").contentType("application/json").header("id",idUser).header("mock",true)
                    .content("{\"from\":{\"latitude\":"+latFrom+",\"longitude\":"+longFrom+" },\"to\":{\"latitude\":"+latTo+",\"longitude\":"+longTo+" },\"form\":{\"filters\":["+myFilter+"], \"green\":false }}")).andReturn();
        });
        Et("il recoit un code de reponse status {int}", (Integer code) -> {
            assertEquals(code, result.getResponse().getStatus());

        });
        Alors("il recoit un message {string} avec un code de reponse {int}", (String message, Integer code) -> {
            assertEquals(code, result.getResponse().getStatus());
            assertEquals(message, result.getResponse().getContentAsString());
        });

        Alors("il recoit un journey sans le transport {string}", (String transp) -> {
            String verif = "\"modeTransport\" : \""+transp.toUpperCase()+"\"";
            assertFalse(result.getResponse().getContentAsString().contains(verif));
        });
        Et("il recoit un journey avec le transport {string}", (String transp) -> {
            String verif = "\"modeTransport\" : \""+transp.toUpperCase()+"\"";
            assertTrue(result.getResponse().getContentAsString().contains(verif));
        });

        Et("il y a une panne sur la ligne {string} de {string}",( String ligne, String transport) -> {

            String notif = "{ \"line\": \""+ligne+"\",\"mode_transport\": \""+transport.toUpperCase()+"\",\"num\": \"2\", \"time\": 30000000 }";

            result=mvc.perform(post("/busshselter/breakdown").contentType("application/json")
                    .content(notif)).andReturn();
        });

        Et("il y a un retard sur la ligne {string} de {string}",( String ligne, String transport) -> {

            String notif = "{ \"line\": \""+ligne+"\",\"mode_transport\": \""+transport.toUpperCase()+"\",\"num\": \"2\", \"time\": 30000000 }";

            result=mvc.perform(post("/busshselter/delay").contentType("application/json")
                    .content(notif)).andReturn();
        });
        Quand("il demande la liste des incidents sur son trajet", () -> {

            result=mvc.perform(get("/incidents?id="+idUser).contentType("application/json")
                    ).andReturn();
        });
        Alors("il recoit un incident sur la ligne {string} du transport {string}", (String ligne, String transport) -> {
            assertNotEquals("[]",result.getResponse().getContentAsString());
            assertTrue(result.getResponse().getContentAsString().contains(transport));

        });
        Alors("il ne recoit pas d'incident", () -> {
            assertEquals("[]",result.getResponse().getContentAsString());

        });
        Et("un expert en mobilité ayant defini des regles grace au dsl {string}", (String regles) -> {

            String program = "{\"program\":\""+regles+"\",\"verifyOnly\":false}";
            result=mvc.perform(post("/program").contentType("application/json")
                    .content(program)).andReturn();
        });
        Quand("il y a de l'avance sur la ligne {string} de {string}", (String ligne, String transport) -> {
            String notif = "{ \"line\": \""+ligne+"\",\"mode_transport\": \""+transport.toUpperCase()+"\",\"num\": \"2\", \"time\": 30000000 }";

            result=mvc.perform(post("/busshselter/fast").contentType("application/json")
                    .content(notif)).andReturn();
        });
        Quand("il y a un bus rempli sur la ligne {string} de {string}", (String ligne, String transport) -> {
            String notif = "{ \"line\": \""+ligne+"\",\"mode_transport\": \""+transport.toUpperCase()+"\",\"num\": \"2\", \"time\": 30000000 }";

            result=mvc.perform(post("/busshselter/full").contentType("application/json")
                    .content(notif)).andReturn();
        });
        Et("les demandes de liberer une place pour l'utilisateur {int} ont echouees", (Integer idUser) -> {
            incidentService.lookImpact(idUser); // lance la demande
            journeyService.cancelDemandsFrom(idUser); // l'annule
        });
        Et("sans la ligne de {string} {string}", (String transport, String ligne) -> {
            assertFalse(result.getResponse().getContentAsString().contains(ligne));

        });

        Et("il a atteint l'etape a la position lat : {double}, longitude {double}", (Double lat, Double lon)->{
            result = mvc.perform(post("/validatedJourneyStep?id="+idUser).contentType("application/json").header("mock",true)
                    .content("{\"endPosition\":{\"latitude\":"+lat+",\"longitude\":"+lon+" },\"form\":{\"filters\":["+myFilter+"], \"green\":false }}")).andReturn();

            assertEquals(result.getResponse().getContentAsString(),(StepState.VALIDATED.toString()));

        });
    }

}
