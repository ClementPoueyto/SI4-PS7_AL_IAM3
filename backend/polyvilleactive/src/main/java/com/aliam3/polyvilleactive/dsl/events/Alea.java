package com.aliam3.polyvilleactive.dsl.events;

/**
 * Classe enum des differents alea qui peuvent survenir. Un rang de priorite est applique pour chaque alea.
 * Ce rang est utilise pour regler les potentiels conflits de regles.
 * @author vivian
 *
 */
public enum Alea {

    PANNE("PANNE", Integer.MAX_VALUE),

    AVANCE("AVANCE", 2),
    NEIGE("NEIGE", 2),
    PLUIE("PLUIE", 2),

    RETARD("RETARD", 1),
    REMPLI("REMPLI", 1),

    SOLEIL("SOLEIL", -1);


    private final String type;
    private final int priority; // the higher the better

    Alea(String type, int priority) {
        this.type = type;
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return type;
    }
}
