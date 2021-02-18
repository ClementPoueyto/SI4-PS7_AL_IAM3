package com.aliam3.polyvilleactive.controller;

import com.aliam3.polyvilleactive.exception.PositionException;
import com.aliam3.polyvilleactive.model.deserializer.PositionDeserializer;
import com.aliam3.polyvilleactive.model.gamification.Demands;
import com.aliam3.polyvilleactive.model.gamification.ResponseDemands;
import com.aliam3.polyvilleactive.model.location.Position;
import com.aliam3.polyvilleactive.model.location.StepState;
import com.aliam3.polyvilleactive.model.transport.Journey;
import com.aliam3.polyvilleactive.model.user.Form;
import com.aliam3.polyvilleactive.service.CoordinateService;
import com.aliam3.polyvilleactive.service.JourneyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class JourneyController {

    @Autowired
    CoordinateService coordinateService;

    @Autowired
    JourneyService journeyService;

    ObjectMapper objectMapper = new ObjectMapper();


    /**
     * route utilise pour demander un trajet et le renvoyer au front mobile
     * @param data
     * @param id
     * @param mock
     * @return reponse http
     */
    @PostMapping(
            value = "/journey", consumes = "application/json", produces = "application/json")
    public ResponseEntity postJourney(@RequestBody String data, @RequestHeader long id, @RequestHeader(required = false) boolean mock) {
        System.out.println(data);
        SimpleModule module =
                new SimpleModule("CustomDeserializer", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(Position.class, new PositionDeserializer());
        objectMapper.registerModule(module);
        JsonNode jsonNode = null;
        Journey res;

        try {
            jsonNode = objectMapper.readTree(data);
            Form form = objectMapper.readValue(jsonNode.get("form").toPrettyString(), Form.class);
            Position from = objectMapper.readValue(jsonNode.get("from").toPrettyString(),Position.class);
            Position to = objectMapper.readValue(jsonNode.get("to").toPrettyString(),Position.class);

            res = journeyService.getJourney(from, to, id, form, mock);

            if(res!=null){
                return ResponseEntity.status(HttpStatus.OK).body(res.toString());
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No journey Found");
            }


        } catch (JsonProcessingException | PositionException
                //| UnknownHostException
                e
        ) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }

    }

    /**
     * route utilise pour faire des demandes aux autres utilisateurs pour ceder leur place
     * @param id
     * @return reponse http
     */
    @GetMapping(value = "/demands", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getDemands(@RequestParam long id)  {
        Demands demands = journeyService.lookForDemands(id);
        if(demands!=null){
            demands.throwto(id);
            return ResponseEntity.status(HttpStatus.OK).body(demands.toString());}
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no demands.");
        }
    }

    /**
     * route utilise pour diffuser les reponses de la demande pour ceder la place
     * @param id
     * @param data
     * @return http reponse
     * @throws JsonProcessingException
     */
    @PostMapping(value = "/demands/response", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Integer> postDemandsResponse(@RequestHeader long id,@RequestBody String data) throws JsonProcessingException {
        ResponseDemands responseDemands = objectMapper.readValue(data,ResponseDemands.class);
        int responseCode= journeyService.saveResponseDemands(responseDemands,id);
        return ResponseEntity.status(HttpStatus.OK).body(responseCode);
    }

    @PostMapping(
            value = "/validatedJourneyStep", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> postValidatedJourneyStep(@RequestBody String data, @RequestParam long id) {
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(data);
            Position position = objectMapper.readValue(jsonNode.get("endPosition").toPrettyString(),Position.class);
            Form form = objectMapper.readValue(jsonNode.get("form").toPrettyString(), Form.class);

            StepState step = journeyService.validateJourneyStep(id,position, form);
            return ResponseEntity.status(HttpStatus.OK).body(step.toString());
        }
        catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }
    }




}
