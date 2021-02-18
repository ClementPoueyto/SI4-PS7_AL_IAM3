package com.aliam3.polyvilleactive.dsl.events.transportation;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.dsl.events.Evenement;
import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;

/**
 * Classe qui represente l'evenement de retard d'un transport
 * @author vivian
 *
 */
public class Retard extends Evenement {

    public Retard(ModeTransport transp) {
        super(transp, Alea.RETARD);
    }

    @Override
    public boolean isTriggeredBy(Incident incident) {
        return incident.triggers(this);
    }

    public String getLigne() {
        return null;
    }

    public String getNum() {
        return null;
    }
}
