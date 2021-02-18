package com.aliam3.polyvilleactive.stepdefs;


import io.cucumber.java8.Fr;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(value = Cucumber.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@CucumberOptions(plugin = {"pretty"}, features = "src/test/resources/features",  glue= {"com.aliam3.polyvilleactive.stepdefs"})
public class AccountStepdefs implements Fr {
    @Autowired
    private MockMvc mvc;
    MvcResult result;

    public AccountStepdefs() {


        Etantdonné("Un utilisateur de l'application mobile",()->{

        });
        Quand("il se connecte en utilisant son pseudo {string} et son mot de passe {string}",
                (String pseudo, String password) -> {
                    result=mvc.perform(post("/login")
                            .content("{\"username\":\""+pseudo+"\",\"password\":\""+password+"\"}")).andReturn();

        });

        Alors("il recoit un code de reponse {int}", (Integer statuscode)->{
            assertEquals(statuscode, result.getResponse().getStatus());
                }
        );
        Et("il recoit les informations de son compte id = {int}", (Integer id)->{
            assertTrue(result.getResponse().getContentAsString().contains("\"id\" : "+id));
                });


        Etantdonné("Un utilisateur de l'application mobile qui n'a pas cree son compte", () -> {
        });

        Alors("il recoit un message d'erreur {string}",(String message)->{
            assertEquals(message, result.getResponse().getContentAsString());
        });

        Etantdonné("un utilisateur qui crée son compte avec les informations : email {string} pseudo {string} password {string}",
                (String email, String pseudo, String password)->{
                    result = mvc.perform(post("/signin").contentType("application/json")
                            .content("{\"email\":\""+email+"\",\"username\":\""+pseudo+"\",\"password\":\""+password+"\"}")).andReturn();
                });


    }

}