package com.aliam3.polyvilleactive.model.incidents.weather;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.dsl.events.weather.RainEvent;
import com.aliam3.polyvilleactive.dsl.events.weather.SnowEvent;
import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;

/**
 * Classe represnentant un incident meteorologique
 * @author vivian
 *
 */
public abstract class IncidentWeather extends Incident {

    protected IncidentWeather(Alea matchingAlea, String line, ModeTransport transport){super(matchingAlea,line,transport);}
    protected IncidentWeather(Alea matchingAlea, String line, ModeTransport transport,String num){super(matchingAlea,line,transport,num);}

    @Override
    public boolean triggers(SnowEvent snow) { return false; }
    
    @Override
    public boolean triggers(RainEvent rain) { return false; }

    @Override
    public String toString() {
        return super.toString();
    }
}
