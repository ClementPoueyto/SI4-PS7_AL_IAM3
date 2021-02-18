package com.aliam3.polyvilleactive.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java8.Fr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class DSL_Stepdefs implements Fr {
    @Autowired
    private MockMvc mvc;
    MvcResult result;

    String program;

    public DSL_Stepdefs() {
        Etantdonné("un utilisateur de l'application web", () -> {

        });

        Quand("il écrit un programme présentant aucune faute", () -> {
            program = "GLOBAL PRIORISER BUS 1 FIN GLOBAL";
        });
        Quand("il écrit un programme présentant une faute lexicale", () -> {
            program = "GLOBAL PRIORISE BUS 1 FIN GLOBAL";
        });
        Quand("il écrit un programme présentant une faute syntaxique", () -> {
            program = "GLOBAL PRIORISER BUS FIN GLOBAL";
        });
        Quand("il écrit un programme présentant une faute sémantique", () -> {
            program = "GLOBAL PRIORISER BUS 1 PRIORISER BUS 2 FIN GLOBAL";
        });

        Et("il valide le programme", () -> {
            result = sendProgram(program, true);
        });

        Et("il reçoit une réponse commençant par {string}", (String debut) -> {
            assertTrue(result.getResponse().getContentAsString().contains(debut));
        });
    }


    MvcResult sendProgram(String program, boolean validateOnly) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode body = objectMapper.createObjectNode();
        body.put("program",program);
        body.put("verifyOnly",validateOnly);

        return mvc.perform(post("/program").content(body.toPrettyString())).andReturn();
    }
}
