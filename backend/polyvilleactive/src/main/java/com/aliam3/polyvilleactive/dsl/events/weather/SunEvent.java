package com.aliam3.polyvilleactive.dsl.events.weather;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.dsl.events.Evenement;
import com.aliam3.polyvilleactive.model.incidents.Incident;

/**
 * Classe qui represente l'evenement du soleil
 * @author vivian
 *
 */
public class SunEvent extends Evenement {
    public SunEvent() {
        super(null, Alea.SOLEIL);
    }

    @Override
    public boolean isTriggeredBy(Incident incident) {
        return incident.triggers(this);
    }
}
