package com.aliam3.polyvilleactive.model.incidents.weather;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;

/**
 * Classe representant un incident pluie
 * @author vivian
 *
 */
public class Rain extends IncidentWeather{


    public Rain (String line, ModeTransport transport){super(Alea.PLUIE,line,transport);}
    public Rain (String line, ModeTransport transport,String num){super(Alea.PLUIE,line,transport,num);}


}
