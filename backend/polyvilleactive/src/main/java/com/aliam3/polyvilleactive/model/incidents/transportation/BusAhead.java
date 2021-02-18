package com.aliam3.polyvilleactive.model.incidents.transportation;

import java.time.Duration;
import java.time.LocalDateTime;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.dsl.events.transportation.Avance;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;

/**
 * Classe represnentant un incident de type avance
 * @author vivian
 *
 */
public class BusAhead extends IncidentTransport {

    BusAhead() {super(Alea.AVANCE);this.time= Duration.ofMinutes(1);}
    public BusAhead( String line, ModeTransport transport){
        super(Alea.AVANCE,line,transport);
        this.time= Duration.ofMinutes(1);
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
    public boolean triggers(Avance avance) {
        boolean sameTransport = avance.getModeTransport().equals(this.modeTransport);
        boolean sameLine = (avance.getLigne()==null)
                || avance.getLigne().equals(this.line);
        boolean sameStop = (avance.getNum()==null)
                || avance.getNum().equals(this.num);
        return sameTransport && sameLine && sameStop;
    }

}
