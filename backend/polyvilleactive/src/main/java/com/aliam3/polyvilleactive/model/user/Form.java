package com.aliam3.polyvilleactive.model.user;

import com.aliam3.polyvilleactive.model.deserializer.FormDeserializer;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui represente les preferences/choix que l'utilisateur a selectionne avant d'envoyer sa requete de trajet
 * @author vivian
 *
 */
@JsonDeserialize(using = FormDeserializer.class)
public class Form {

    List<ModeTransport>  filters;
    boolean green;

    public Form(){
        this.filters= new ArrayList<>();
        green= false;
    }

    public Form(List<ModeTransport> filters, boolean green) {
        this.filters = filters;
        this.green= green;
    }

    public boolean isGreen() {
        return green;
    }

    public void setGreen(boolean green) {
        this.green = green;
    }

    public List<ModeTransport> getFilters() {
        return filters;
    }

    public void setFilters(List<ModeTransport> filters) {
        this.filters = filters;
    }
}
