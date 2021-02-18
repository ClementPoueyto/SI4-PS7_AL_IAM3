package com.aliam3.polyvilleactive.model.transport;

/**
 *  Enum qui definit tout les moyens de transport possible.
 * @author vivian
 * @author clement
 */
public enum ModeTransport {

    WALKING("WALKING"),
    BIKE("BIKE"),
    METRO("METRO"),
    BUS("BUS"),
    TRAMWAY("TRAMWAY"),
    TRAIN("TRAIN"),
    WAITING("WAITING"),
    PUBLIC_TRANSPORT("PUBLIC_TRANSPORT");

    private final String transport;

    ModeTransport(String transport){
        this.transport=transport;
    }

    public String getTransport(){
        return transport;
    }

    @Override
    public String toString() {
        return transport;
    }

}