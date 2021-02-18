package com.aliam3.polyvilleactive.dsl.events.weather;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.dsl.events.Evenement;
import com.aliam3.polyvilleactive.model.incidents.Incident;

/**
 * Classe qui represente l'evenement de la neige
 * @author vivian
 *
 */
public class SnowEvent extends Evenement {
    public SnowEvent() {
        super(null, Alea.NEIGE);
    }

    @Override
    public boolean isTriggeredBy(Incident incident) {
        return incident.triggers(this);
    }
}
