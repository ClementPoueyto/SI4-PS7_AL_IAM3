package com.aliam3.polyvilleactive.model.incidents.transportation;

import java.time.Duration;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.model.deserializer.DurationDeserializer;
import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Classe represnentant un incident de transport
 * @author vivian
 *
 */
public abstract class IncidentTransport extends Incident{

	@JsonProperty("time")
	@JsonDeserialize(using = DurationDeserializer.class)
	protected Duration time;

	protected IncidentTransport(Alea matchingAlea){ super(matchingAlea);}
    protected IncidentTransport(Alea matchingAlea, String line, ModeTransport transport){super(matchingAlea,line,transport);}
    protected IncidentTransport(Alea matchingAlea, String line, ModeTransport transport,String num){super(matchingAlea,line,transport,num);}

	@Override
	public String toString() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return "{}";
	}

	public Duration getTime() {
		return time;
	}

}
