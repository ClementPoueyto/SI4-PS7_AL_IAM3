package com.aliam3.polyvilleactive.model.incidents.transportation;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.dsl.events.transportation.Retard;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Classe represnentant un incident de type retard
 * 
 * @author vivian
 *
 */
public class Delay extends IncidentTransport {

	public Delay() {
		super(Alea.RETARD);
	}

	public Delay(Duration time, String line, ModeTransport transport) {
		super(Alea.RETARD, line, transport);
		this.time = time;

	}

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

	@Override
	public boolean triggers(Retard retard) {
		boolean sameTransport = retard.getModeTransport().equals(this.modeTransport);
		boolean sameLine = (retard.getLigne() == null) || retard.getLigne().equals(this.line);
		boolean sameStop = (retard.getNum() == null) || retard.getNum().equals(this.num);
		return sameTransport && sameLine && sameStop;
	}

	@Override
	public boolean isPassed() {
		return LocalDateTime.now().compareTo(this.timestamp.plus(this.time)) > 0;
	}

	public void setTime(Duration time) {
		this.time = time;
	}
}
