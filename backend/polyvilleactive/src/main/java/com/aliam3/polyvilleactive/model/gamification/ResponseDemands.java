package com.aliam3.polyvilleactive.model.gamification;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe qui représente la reponse de la demande envoyee auparavant
 * @author vivian
 *
 */
public class ResponseDemands {
    @JsonProperty("accepted")
    boolean accepted=false;

    @JsonProperty("idDemands")
    long idDemands;

    ResponseDemands(){}
    public ResponseDemands(long idDemands, boolean wasAccepted) {
        this.idDemands = idDemands;
        this.accepted = wasAccepted;
    }

    public long getIdDemands() {
        return idDemands;
    }

    public boolean wasAccepted(){
        return accepted;
    }
}
