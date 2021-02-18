package com.aliam3.polyvilleactive.model.deserializer;

import com.aliam3.polyvilleactive.model.incidents.weather.IncidentWeather;
import com.aliam3.polyvilleactive.model.incidents.weather.Rain;
import com.aliam3.polyvilleactive.model.incidents.weather.Snow;
import com.aliam3.polyvilleactive.model.incidents.weather.Sunny;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IncidentWeatherDeserializerTest {

    @Test
    void deserialize() throws JsonProcessingException {
        final String jsonRainIncident="{ \"type\":\"Rain\", \"mode_transport\":\"BUS\",\"line\":\"Champs de Mars - Prov\",\"num\":\"132A\"}";
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(IncidentWeather.class, new IncidentWeatherDeserializer());
        mapper.registerModule(module);
        IncidentWeather meteo = mapper.readValue(jsonRainIncident, IncidentWeather.class);
        assertEquals(ModeTransport.BUS,meteo.getModeTransport());
        assertTrue(meteo.getLine().equals("Champs de Mars - Prov"));
        assertTrue(meteo instanceof Rain);
        final String jsonSnowIncident="{ \"type\":\"Snow\", \"mode_transport\":\"METRO\",\"line\":\"Prov - Champs de Mars\",\"num\":\"132A\"}";
        meteo = mapper.readValue(jsonSnowIncident, IncidentWeather.class);
        assertEquals(ModeTransport.METRO,meteo.getModeTransport());
        assertTrue(meteo.getLine().equals("Prov - Champs de Mars"));
        assertTrue(meteo instanceof Snow);
        final String jsonSunnyIncident="{ \"type\":\"Sunny\", \"mode_transport\":\"TRAIN\",\"line\":\"Champs de Mars - Prov\",\"num\":\"132A\"}";
        meteo = mapper.readValue(jsonSunnyIncident, IncidentWeather.class);
        assertEquals(ModeTransport.TRAIN,meteo.getModeTransport());
        assertTrue(meteo.getLine().equals("Champs de Mars - Prov"));
        assertTrue(meteo instanceof Sunny);
    }
}