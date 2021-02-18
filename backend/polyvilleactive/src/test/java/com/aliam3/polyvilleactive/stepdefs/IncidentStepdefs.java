package com.aliam3.polyvilleactive.stepdefs;

import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.incidents.weather.Snow;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import io.cucumber.java8.Fr;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(value = Cucumber.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@CucumberOptions(plugin = {"pretty"}, features = "src/test/resources/features",  glue= {"com.aliam3.polyvilleactive.stepdefs"})
public class IncidentStepdefs implements Fr{

    @Autowired
    private MockMvc mvc;
    MvcResult result;
    Incident incident;
    String type;
    public IncidentStepdefs() {
        Etantdonné("un abribus qui detecte un changement meteo {string} sur la ligne {string} de {string}", (String meteo, String ligne, String transport) -> {
            if(meteo.equals("neige")){
                type = "Snow";
                incident = new Snow(ligne, ModeTransport.valueOf(transport));
            }

        });
        Quand("il envoie la requete a l'application",()->{
            result = mvc.perform(post("/busshselter/weather").contentType("application/json")
                    .content(
                            "{\"time\":60000,\"line\":\""+incident.getLine()+"\",\"num\":\"2\",\"type\":\""+type+"\",\"mode_transport\":\""+incident.getModeTransport()+"\",\"timestamp\":{\"year\":2021,\"month\":1,\"date\":7,\"hours\":10,\"minutes\":50,\"seconds\":31,\"milliseconds\":214}}"                    )).andReturn();


        });
        Alors("un incident {string} est cree sur la ligne {string} de {string}",(String meteo, String ligne, String transport)->{
            assertEquals(200, result.getResponse().getStatus());
            assertTrue(result.getResponse().getContentAsString().contains(ligne));
            assertTrue(result.getResponse().getContentAsString().contains(transport));

        });
    }
}
