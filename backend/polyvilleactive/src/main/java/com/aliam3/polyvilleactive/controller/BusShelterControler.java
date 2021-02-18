package com.aliam3.polyvilleactive.controller;

import com.aliam3.polyvilleactive.model.deserializer.IncidentWeatherDeserializer;
import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.incidents.transportation.*;
import com.aliam3.polyvilleactive.model.incidents.weather.IncidentWeather;
import com.aliam3.polyvilleactive.service.IncidentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BusShelterControler  {

    @Autowired
    IncidentService incidentService;

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * envoi d'un Alea retard de l'objet connecté au backend
     * @param data
     * @return le retard
     * @throws JsonProcessingException
     */
    @PostMapping(value = "/busshselter/delay", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Delay postDelay(@RequestBody String data) throws JsonProcessingException {

        Delay delay= objectMapper.readValue(data, Delay.class);
        incidentService.updateList(delay);
       return delay;
    }

    /**
     * renvoie la liste des incidents impactant le trajet actuel de l'utilisateur
     * @param id
     * @return liste des incidents impactants
     */
    @GetMapping(value = "/incidents", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getIncidents(@RequestParam long id)  {
        List<Incident> incidents = incidentService.lookImpact(id);
        return ResponseEntity.status(HttpStatus.OK).body(incidents.toString());
    }

    /**
     * envoi d'un Alea rempli de l'objet connecté au backend
     * @param data
     * @return reponse http
     * @throws JsonProcessingException
     */
    @PostMapping(value = "/busshselter/full", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity postFull(@RequestBody String data) throws JsonProcessingException {

        FullPeople fullPeopleIncident= objectMapper.readValue(data, FullPeople.class);
        incidentService.updateList(fullPeopleIncident);
        return ResponseEntity.status(HttpStatus.OK).body(fullPeopleIncident.toString());
    }
    
    /**
     * envoi d'un Alea avance de l'objet connecté au backend
     * @param data
     * @return reponse HTTP
     * @throws JsonProcessingException
     */
    @PostMapping(value = "/busshselter/fast", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity postFast(@RequestBody String data) throws JsonProcessingException {

        BusAhead busAheadIncident= objectMapper.readValue(data, BusAhead.class);
        incidentService.updateList(busAheadIncident);
        return ResponseEntity.status(HttpStatus.OK).body(busAheadIncident.toString());
    }


    /**
     * envoi d'un Alea meteo de l'objet connecté au backend
     * @param data
     * @return reponse http
     * @throws JsonProcessingException
     */
    @PostMapping(value = "/busshselter/weather", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity postMeteo(@RequestBody String data) throws JsonProcessingException {

        System.out.println(data.toString());
       ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(IncidentWeather.class, new IncidentWeatherDeserializer());
        mapper.registerModule(module);
        IncidentWeather meteo = mapper.readValue(data, IncidentWeather.class);
        incidentService.updateList(meteo);
        return ResponseEntity.status(HttpStatus.OK).body(meteo.toString());
    }

    /**
     * envoi d'un Alea panne de l'objet connecté au backend
     * @param data
     * @return reponse http
     * @throws JsonProcessingException
     */
    @PostMapping(value = "/busshselter/breakdown", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity postBreakdown(@RequestBody String data) throws JsonProcessingException {

        Breakdown breakdown= objectMapper.readValue(data, Breakdown.class);
        incidentService.updateList(breakdown);
        return ResponseEntity.status(HttpStatus.OK).body(breakdown.toString());
    }


}
