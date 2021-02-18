package com.aliam3.polyvilleactive.model.incidents.transportation;


import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;

import java.time.Duration;
import java.time.LocalDateTime;

import com.aliam3.polyvilleactive.dsl.events.transportation.Panne;

/**
 * Classe represnentant un incident de type panne
 * @author vivian
 *
 */
public class Breakdown extends IncidentTransport {

	private final int TEMPS_PANNE= 1;
    Breakdown(){
        super(Alea.PANNE);
        this.time= Duration.ofHours(TEMPS_PANNE);
    }

    public Breakdown(String line, ModeTransport transport){
        super(Alea.PANNE,line,transport);
        this.time= Duration.ofHours(TEMPS_PANNE);
    }

	@Override
	public boolean isPassed() {
        return LocalDateTime.now().compareTo(this.timestamp.plus(this.time)) > 0;
    }

    @Override
    public boolean triggers(Panne panne) {
        boolean sameTransport = panne.getModeTransport().equals(this.modeTransport);
        boolean sameLine = (panne.getLigne()==null)
                || panne.getLigne().equals(this.line);
        return sameTransport && sameLine;
    }



}
