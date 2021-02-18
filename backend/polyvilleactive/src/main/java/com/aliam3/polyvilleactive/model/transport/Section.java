package com.aliam3.polyvilleactive.model.transport;

import com.aliam3.polyvilleactive.model.deserializer.SectionDeserializer;
import com.aliam3.polyvilleactive.model.location.Place;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

/**
 * Classe representant une etape d'un trajet
 * @author vivian
 * @author clement
 */
@JsonDeserialize(using = SectionDeserializer.class)
public class Section {
    private Place from;
    private Place to;

    private long duration;

    private LocalDateTime arrivalDateTime;
    private LocalDateTime departureDateTime;

    private Transport transport;
    private double co2emission;

    private boolean reached;

    public Section() {
        reached = false;
    }


    @Override
    public String toString() {
        return "Section{" +
                "from=" + from +
                ", to=" + to +
                ", duration=" + duration +
                ", arrivalDateTime=" + arrivalDateTime +
                ", departureDateTime=" + departureDateTime +
                ", co2emission=" + co2emission +
                '}';
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Place getFrom() {
        return from;
    }

    public void setFrom(Place from) {
        this.from = from;
    }

    public Place getTo() {
        return to;
    }

    public void setTo(Place to) {
        this.to = to;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }



    public double getCo2emission() {
        return co2emission;
    }

    public void setCo2emission(double co2emission) {
        this.co2emission = co2emission;
    }

    public boolean isReached() {
        return reached;
    }

    public void setReached(boolean reached) {
        this.reached = reached;
    }
}
