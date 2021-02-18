package com.aliam3.polyvilleactive.model.incidents.weather;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.dsl.events.weather.SunEvent;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;

/**
 * Classe representant un incident soleil
 * @author vivian
 *
 */
public class Sunny extends IncidentWeather{


    public Sunny(String line, ModeTransport modeTransport){ super(Alea.SOLEIL,line,modeTransport);}
    public Sunny(String line, ModeTransport transport,String num){super(Alea.SOLEIL,line,transport,num);}


    @Override
    public boolean triggers(SunEvent sun) {
        return true;
    }

}
