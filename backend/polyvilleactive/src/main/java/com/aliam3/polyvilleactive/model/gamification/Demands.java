package com.aliam3.polyvilleactive.model.gamification;

import com.aliam3.polyvilleactive.model.transport.Transport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui représente un demande pour une action spécifique ( ici ceder sa place)
 * @author vivian
 *
 */
public class Demands {
    Transport transport;
    long id;
    long idUserAsking;
    boolean accepted=false;
    List<Long> throwedto=new ArrayList<>();
    private static final Duration DEMAND_DURATION = Duration.ofSeconds(10);
    private LocalDateTime expirationDate;
    private boolean hasEnded = false;

    public Demands(){}

    public Demands(long idUserAsking, Transport transport){
        this.id=LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        this.idUserAsking=idUserAsking;
        this.transport=transport;
        this.expirationDate=LocalDateTime.now().plus(DEMAND_DURATION);
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public void setIdUserAsking(long idUserAsking) {
        this.idUserAsking = idUserAsking;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdUserAsking() {
        return idUserAsking;
    }

    public long getId() {
        return id;
    }

    public void accept(){accepted=true;}

    public boolean wasAccepted(){return  accepted;}

    public void throwto(long id){throwedto.add(id);}

    public boolean wasThrowedTo(long id){return throwedto.contains(id);}

    public void end() {
        hasEnded = true;
    }

    public boolean hasEnded() {
        // Can be manually ended or can expire
        return hasEnded || LocalDateTime.now().compareTo(expirationDate) > 0;
    }
}
