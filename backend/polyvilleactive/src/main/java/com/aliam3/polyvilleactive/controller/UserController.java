package com.aliam3.polyvilleactive.controller;

import com.aliam3.polyvilleactive.model.user.User;
import com.aliam3.polyvilleactive.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * route utilise pour recuperer le profil d'un utilisateur 
     * @param id
     * @return le profil de l'utilisateur
     */
    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<String> getUser(@RequestParam long id){


        Optional<User> user = userService.searchForUser(id);
        return user.map(value -> ResponseEntity.status(HttpStatus.OK).body(value.toString()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("no user found"));

    }

    /**
     * route pour verifier la connexion a un compte
     * @param body
     * @return reponse de la connexion au compte
     * @throws JsonProcessingException
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody String body) throws JsonProcessingException {
        JsonNode jsonNode = null;
        jsonNode = objectMapper.readTree(body);
        String username =jsonNode.get("username").asText();
        String password =jsonNode.get("password").asText();

        Optional<User> user = userService.searchForUser(username,password);
        return user.map(value -> ResponseEntity.status(HttpStatus.OK).body(value.toString()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no user found"));

    }

    /**
     * route utilise pour s'inscrire a l'application
     * @param body
     * @return reponse de l'inscription
     * @throws JsonProcessingException
     */
    @PostMapping(value = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity signin(@RequestBody String body) throws JsonProcessingException {
        User user = objectMapper.readValue(body,User.class);
        Optional<User> finalUser =userService.signupUser(user);
        if(finalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or password empty.");
        }

    }

}
