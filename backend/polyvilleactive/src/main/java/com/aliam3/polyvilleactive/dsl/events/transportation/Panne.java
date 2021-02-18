package com.aliam3.polyvilleactive.dsl.events.transportation;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.dsl.events.Evenement;
import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;

/**
 * Classe qui represente l'evenement d'une panne d'un transport
 * @author vivian
 *
 */
public class Panne extends Evenement {

    public Panne(ModeTransport transp) {
        super(transp, Alea.PANNE);
    }

    @Override
    public boolean isTriggeredBy(Incident incident) {
        return incident.triggers(this);
    }
    
    public String getLigne() {
        return null;
    }
}

