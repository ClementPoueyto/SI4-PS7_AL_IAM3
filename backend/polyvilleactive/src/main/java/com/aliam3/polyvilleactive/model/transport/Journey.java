package com.aliam3.polyvilleactive.model.transport;

import com.aliam3.polyvilleactive.model.deserializer.JourneyDeserializer;
import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.location.Place;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Classe qui represente un trajet d'un utilisateur
 * @author vivian
 *
 */
@JsonDeserialize(using = JourneyDeserializer.class)
public class Journey {
    private Place arrival;
    private Place departure;
    private LocalDateTime dateDeparture;
    private LocalDateTime dateArrival;
    private long duration;
    private List<Section> sections;
    private int nbTransfers;
    private double fare;
    private double co2emission;
    private Map<ModeTransport,Long> transports;
    private long idUser;

    public Journey() {/* Default Constructor */}
    public Journey(int idUser){this.idUser=idUser;}//Constructor testing
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
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Place getArrival() {
        return arrival;
    }

    public void setArrival(Place arrival) {
        this.arrival = arrival;
    }

    public Place getDeparture() {
        return departure;
    }

    public void setDeparture(Place departure) {
        this.departure = departure;
    }

    public Map<ModeTransport, Long> getTransports() {
        return transports;
    }

    public void setTransports(Map<ModeTransport, Long> transports) {
        this.transports = transports;
    }

    public LocalDateTime getDateDeparture() {
        return dateDeparture;
    }

    public void setDateDeparture(LocalDateTime dateDeparture) {
        this.dateDeparture = dateDeparture;
    }

    public LocalDateTime getDateArrival() {
        return dateArrival;
    }

    public void setDateArrival(LocalDateTime dateArrival) {
        this.dateArrival = dateArrival;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public int getNbTransfers() {
        return nbTransfers;
    }

    public void setNbTransfers(int nbTransfers) {
        this.nbTransfers = nbTransfers;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public double getCo2emission() {
        return co2emission;
    }

    public void setCo2emission(double co2emission) {
        this.co2emission = co2emission;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdUser() {
        return idUser;
    }

    /**
     * regarde si le trajet est impacte par l'incident
     * @param incident
     * @return true si le trajet est impacte par l'incident
     */
    public boolean isAffectedBy(Incident incident){
        return incident.affects(this);
    }


    /**
     * Retrieves the current section of the journey
     * @return the journey's section the user is currently at,
     *         null if ended
     */
    public Section getCurrentStep(){
        Optional<Section> candidate = sections.stream()
                .filter(s -> !s.getTransport().getModeTransport().equals(ModeTransport.WAITING)) // filter out waiting
                .filter(s -> !s.isReached())
                .findFirst();
		return candidate.orElse(null);
	}
}
