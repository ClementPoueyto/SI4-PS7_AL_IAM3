package com.aliam3.polyvilleactive.service;

public enum NavitiaDatasources {
    ILE_DE_FRANCE("coverage/fr-idf/"),
    NORTH_EAST("coverage/fr-ne/"),
    NORTH_WEST("coverage/fr-nw/"),
    NORMANDIE("coverage/fr-nw/"),
    NONE("");

    private final String ds;

    NavitiaDatasources(String ds){
        this.ds=ds;
    }

    public String getDatasource(){
        return ds;
    }

    @Override
    public String toString() {
        return ds;
    }
}

