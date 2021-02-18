package com.aliam3.polyvilleactive.model.incidents.transportation;

import java.time.Duration;
import java.time.LocalDateTime;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.dsl.events.transportation.Rempli;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;

/**
 * Classe represnentant un incident de type rempli
 * 
 * @author vivian
 *
 */
public class FullPeople extends IncidentTransport {

	FullPeople() {
		super(Alea.REMPLI);
		this.time= Duration.ofMinutes(5);
	}

	public FullPeople(String line, ModeTransport transport) {
		super(Alea.REMPLI, line, transport);
		this.time= Duration.ofMinutes(5);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public boolean isPassed() {
		return LocalDateTime.now().compareTo(this.timestamp.plus(this.time)) > 0;
	}

	@Override
	public boolean triggers(Rempli rempli) {
		boolean sameTransport = rempli.getModeTransport().equals(this.modeTransport);
		boolean sameLine = (rempli.getLigne() == null) || rempli.getLigne().equals(this.line);
		boolean sameStop = (rempli.getNum() == null) || rempli.getNum().equals(this.num);
		return sameTransport && sameLine && sameStop;

	}

}
