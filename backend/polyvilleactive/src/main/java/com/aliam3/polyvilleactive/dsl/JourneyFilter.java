package com.aliam3.polyvilleactive.dsl;

import com.aliam3.polyvilleactive.model.transport.Journey;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe qui s'occupe de filtrer les differents trajets selon les priorités données.
 * @author vivian
 *
 */
public class JourneyFilter {

	private List<Priorite> priorities;
	private List<Prohibition> prohibitions;

	public JourneyFilter(Action action) {
		this(action.getPriorites(),action.getProhibitions());
	}

	public JourneyFilter(List<Priorite> priorities, List<Prohibition> prohibitions) {
		this.priorities=priorities;
		this.prohibitions=prohibitions;
	}
	
	/**
	 * Sorts journeys according to given preferences.
	 * The fist element will be the more suitable
	 * The last element will be the less suitable
	 * @param journeys list of journeys
	 */
	public List<Journey> applyPreferences(List<Journey> journeys) {
		Map<Journey, Integer> values = new HashMap<>();
		for (Journey journey : journeys) {
			values.put(journey, computeJourneyValue(journey));
		}

		journeys.sort(Comparator.comparingInt((Journey journey) -> values.getOrDefault(journey, 0)).reversed());
		return journeys;
	}

	/**
	 * Filters-out journeys with prohibited sections
	 * @param journeys list of journeys
	 */
	public List<Journey> applyProhibitions(List<Journey> journeys) {
		List<ModeTransport> prohibited = prohibitions.stream()
				.map(p -> p.transport)
				.collect(Collectors.toList());

		List<Journey> result = new ArrayList<>(journeys);

		for (Journey j : journeys) {
			for (Map.Entry<ModeTransport,Long> entry : j.getTransports().entrySet()) {
				if (prohibited.contains(entry.getKey())) {
					result.remove(j);
				}
			}
		}

		return result;
	}

	public List<Journey> apply(List<Journey> newJourneys) {
		return applyPreferences(applyProhibitions(newJourneys));
	}



	// ---------------------------------------------------------

	/**
	 * Internally assigns a value to a journey based on stored priorities
	 * @param journey the journey
	 * @return the total priority estimated for the given journey
	 */
	private Integer computeJourneyValue(Journey journey) {
		int value = 0;
		for (Map.Entry<ModeTransport, Long> entry : journey.getTransports().entrySet()) {
			ModeTransport transport = entry.getKey();
			Long time = entry.getValue();

			int transportRank = findRankForTransport(transport);
			value += time * transportRank;
		}
		return value;
	}

	/**
	 * Finds the internally specified rank for a given transport
	 * @param transport a transport mode
	 * @return 0 if the priority wasn't specified else it's priority
	 */
	private int findRankForTransport(ModeTransport transport) {
		Priorite priorityOnTransport = priorities.stream()
				.filter((Priorite p) -> p.transport.equals(transport))
				.findFirst()
				.orElse(null);
		return (priorityOnTransport!=null) ? priorityOnTransport.rang : 0;
	}
}
