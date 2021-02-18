package com.aliam3.polyvilleactive.dsl.events.weather;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.dsl.events.Evenement;
import com.aliam3.polyvilleactive.model.incidents.Incident;

/**
 * Classe qui represente l'evenement de la pluie
 * @author vivian
 *
 */
public class RainEvent extends Evenement {
    public RainEvent() {
        super(null, Alea.PLUIE);
    }

    @Override
    public boolean isTriggeredBy(Incident incident) {
        return incident.triggers(this);
    }
}
