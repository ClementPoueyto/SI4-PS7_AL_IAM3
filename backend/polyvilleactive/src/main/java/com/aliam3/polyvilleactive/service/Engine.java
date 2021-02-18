package com.aliam3.polyvilleactive.service;

import com.aliam3.polyvilleactive.MemoryRule;
import com.aliam3.polyvilleactive.dsl.*;
import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.incidents.transportation.IncidentTransport;
import com.aliam3.polyvilleactive.model.incidents.weather.IncidentWeather;
import com.aliam3.polyvilleactive.model.transport.Journey;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe qui gere les fonctionnalites du moteur, avec la gestion des filtres et
 * regles de conflit
 * 
 * @author vivian
 * @author clement
 */
public class Engine {

	private final Comparator<Journey> co2Comparator = Comparator.comparingDouble(Journey::getCo2emission);

	public Engine() {
		/* Default Constructor */}

	/**
	 * Selects a new journey for a requester by taking into account set up rules
	 * 
	 * @param id                 requester's id
	 * @param lastJourney        current requester's journey before selection (null
	 *                           if first request)
	 * @param newJourneys        all new journeys available for the requester
	 * @param incidentsTransport all reported incidents related to transportation
	 * @param incidentsWeather   all reported incidents related to the weather
	 * @param prohibitions       prohibitions from mobile form
	 * @return
	 */
	public Journey selectJourney(long id, Journey lastJourney, List<Journey> newJourneys,
			List<IncidentTransport> incidentsTransport, List<IncidentWeather> incidentsWeather,
			List<Prohibition> prohibitions, boolean greenMode) {
		newJourneys = new JourneyFilter(null, prohibitions).applyProhibitions(newJourneys);
		List<Incident> incidents = new ArrayList<>(incidentsTransport);
		incidents.addAll(incidentsWeather);
		List<Incident> impactingIncidents = this.getIncidentsWhichAffected(lastJourney, incidents);
		List<Regle> rulesToTrigger = this.findValidatedRules(MemoryRule.RULES, impactingIncidents);
		Action selectedAction = this.getActionAfterIncidents(rulesToTrigger);
		newJourneys = new JourneyFilter(selectedAction).apply(newJourneys);

		if (greenMode)
			Collections.sort(newJourneys, co2Comparator);

		Journey selectedJourney = this.getNonAffectedJourneyFrom(newJourneys, incidentsTransport);

		if (selectedJourney != null) {
			selectedJourney.setIdUser(id);
		}
		return selectedJourney;
	}

	/**
	 * @param failedJourney current requester's journey before selection (null if
	 *                      first request)
	 * @param incidents     all reported incidents
	 * @return list of incident candidates that would have caused the journey to
	 *         fail
	 */
	List<Incident> getIncidentsWhichAffected(Journey failedJourney, List<Incident> incidents) {
		if (failedJourney == null)
			return List.of();
		return incidents.stream().filter(failedJourney::isAffectedBy).collect(Collectors.toList());
	}

	/**
	 * @param triggeredRules list of rules that were triggered
	 * @return A filter suitable according to the given rules
	 */
	Action getActionAfterIncidents(List<Regle> triggeredRules) {

		Optional<Integer> maxAleaPriority = triggeredRules.stream().map(r -> r.getEvent().getAlea().getPriority())
				.max(Comparator.naturalOrder());

		if (maxAleaPriority.isPresent()) {
			triggeredRules = triggeredRules.stream()
					.filter(r -> r.getEvent().getAlea().getPriority() == maxAleaPriority.get())
					.collect(Collectors.toList());
		}

		if (triggeredRules.isEmpty()) {
			return new Action(MemoryRule.GLOBALPRIORITIZE, new ArrayList<>());
		} else if (triggeredRules.size() == 1) {
			return triggeredRules.get(0).getAction();
		} else {
			List<Priorite> allPriorities = new ArrayList<>();
			triggeredRules.forEach(r -> allPriorities.addAll(r.getAction().getPriorites()));
			List<Priorite> selectedPriorities = Priorite.compromise(allPriorities);
			List<Prohibition> allProhibitions = new ArrayList<>();
			triggeredRules.forEach(r -> allProhibitions.addAll(r.getAction().getProhibitions()));
			return new Action(selectedPriorities, allProhibitions);
		}
	}

	/**
	 * Finds Rules that are validated by any of the given incidents
	 * 
	 * @param rules     registered incidents
	 * @param incidents all registered incidents
	 * @return rules affected
	 */
	List<Regle> findValidatedRules(List<Regle> rules, List<Incident> incidents) {
		List<Regle> validatedRules = new ArrayList<>();
		for (Regle regle : rules) {
			for (Incident incident : incidents) {
				if (regle.isAffectedBy(incident))
					validatedRules.add(regle);
			}
		}
		return validatedRules;
	}

	/**
	 *
	 * @param newJourneys filtered journeys available for the requester
	 * @param incidents   all reported incidents
	 * @return a journey not affected by any reported incident
	 */
	Journey getNonAffectedJourneyFrom(List<Journey> newJourneys, List<IncidentTransport> incidents) {
		// On choisit le premier trajet qui n'est plus affecté par aucun incident
		for (Journey journey : newJourneys) {
			boolean accepted = true;
			for (Incident incident : incidents) {
				if (journey.isAffectedBy(incident)) {

					accepted = false;
				}
			}
			if (accepted) {
				return journey;
			}
		}
		return null;
	}
}
