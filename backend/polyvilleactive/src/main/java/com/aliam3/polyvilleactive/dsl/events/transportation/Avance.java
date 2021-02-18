package com.aliam3.polyvilleactive.dsl.events.transportation;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.dsl.events.Evenement;
import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;

/**
 * Classe qui represente l'evenement d'avance d'un transport
 * @author vivian
 *
 */
public class Avance extends Evenement {

	public Avance(ModeTransport transp) {
		super(transp, Alea.AVANCE);
	}

    public String getLigne() {
        return null;
    }
	@Override
	public boolean isTriggeredBy(Incident incident) {
		return incident.triggers(this);
	}
	
    public String getNum() {
        return null;
    }

}
