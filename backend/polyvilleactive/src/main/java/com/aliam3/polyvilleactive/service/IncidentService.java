package com.aliam3.polyvilleactive.service;

import com.aliam3.polyvilleactive.MemoryRule;
import com.aliam3.polyvilleactive.dsl.Regle;
import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.model.gamification.Demands;
import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.incidents.transportation.IncidentTransport;
import com.aliam3.polyvilleactive.model.incidents.weather.IncidentWeather;
import com.aliam3.polyvilleactive.model.transport.Journey;

import com.aliam3.polyvilleactive.model.transport.Section;
import com.aliam3.polyvilleactive.model.transport.Transport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui gere les differents incidents
 * 
 * @author vivian
 * @author clement
 */
@EnableScheduling
@Service
public class IncidentService {

	@Autowired
	JourneyService journeyService;

	public List<IncidentWeather> incidentsWeather = new ArrayList<>();

	public List<IncidentTransport> incidents = new ArrayList<>();

	/**
	 * met a jour la liste des incidents de transport
	 * 
	 * @param incident
	 */
	public void updateList(IncidentTransport incident) {
		incidents.removeIf(incident1 -> incident.getLine().equals(incident1.getLine())
				&& incident.getModeTransport() == incident1.getModeTransport());
		incidents.add(incident);
	}

	/**
	 * met a jour la liste des incidents de transport
	 * 
	 * @param incident
	 */
	public void updateList(IncidentWeather incident) {
		incidentsWeather.removeIf(incident1 -> incident.getLine().equals(incident1.getLine())
				&& incident.getModeTransport() == incident1.getModeTransport());
		incidentsWeather.add(incident);
	}

	/**
	 * regarde la liste des incidents stockes qui peuvent impacter le trajet
	 * 
	 * @param id de l'utilisateur
	 * @return liste des incidents impactant le trajet
	 */
	public List<Incident> lookImpact(long id) {
		boolean isRulePresent = false;

		List<Incident> problems = new ArrayList<>();
		for (IncidentTransport incident : incidents) {
			for (Journey journey : journeyService.journeyList) {
				if (journey.getIdUser() == id && journey.isAffectedBy(incident)) {
					if (incident.getAlea().equals(Alea.REMPLI)) {

						Transport fullTransport = null; // should exist if we get here
						for (Section section : journey.getSections()) {
							if (incident.affects(section))
								fullTransport = section.getTransport();
						}

						Demands demand = new Demands(journey.getIdUser(), fullTransport);

						Demands stored = journeyService.getDemandsIfExists(demand);
						if (stored == null) {
							// Pas de demande, on ajoute la demande et ne signale pas l'incident pour le
							// moment
							journeyService.addDemands(demand);
						} else {
							if (!stored.wasAccepted() && stored.hasEnded()) {
								System.out.println(demand.toString());
								problems.add(incident);
							}
						}

					} else {
						problems.add(incident);
					}
				}
			}
		}

		for (IncidentWeather incident : incidentsWeather) {
			for (Journey journey : journeyService.journeyList) {
				if (journey.getIdUser() == id && journey.isAffectedBy(incident)) {
					for (Regle r : MemoryRule.RULES) {
						if (r.getEvent().getAlea().equals(incident.getAlea())) {
							isRulePresent = true;
						}
					}
					if (isRulePresent)
						problems.add(incident);
				}
			}
		}
		return problems;
	}

	@Scheduled(fixedDelay = 20000)
	public void cleanIncident() {
		System.out.println(incidents);
		cleanDelay();
		System.out.println(incidents);

	}

	public void cleanDelay() {
		List<IncidentTransport> newIncidents = new ArrayList<>();
		for (IncidentTransport incident : incidents) {
			if (!incident.isPassed()) {
				newIncidents.add(incident);
			}
		}
		incidents = newIncidents;
	}

}
