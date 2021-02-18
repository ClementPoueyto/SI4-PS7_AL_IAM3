package com.aliam3.polyvilleactive.model.deserializer;

import com.aliam3.polyvilleactive.model.incidents.weather.IncidentWeather;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DurationDeserializerTest {

    @Test
    void deserialize() throws JsonProcessingException {

        final String jsonDuration="5000";
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Duration.class, new DurationDeserializer());
        mapper.registerModule(module);
        Duration duration = mapper.readValue(jsonDuration, Duration.class);
        assertNotNull(duration);
        assertEquals(5,duration.getSeconds());
    }
}