package com.aliam3.polyvilleactive.model.incidents.weather;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.dsl.events.weather.SnowEvent;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;

/**
 * Classe representant un incident neige
 * @author vivian
 *
 */
public class Snow extends IncidentWeather{


    public Snow (String line, ModeTransport modeTransport){ super(Alea.NEIGE,line,modeTransport);}
    public Snow(String line, ModeTransport transport,String num){super(Alea.NEIGE,line,transport,num);}

    @Override
    public boolean triggers(SnowEvent snow) {
        return true;
    }


}
