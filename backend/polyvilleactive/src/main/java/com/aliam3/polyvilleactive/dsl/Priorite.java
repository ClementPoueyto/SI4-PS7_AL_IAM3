package com.aliam3.polyvilleactive.dsl;

import com.aliam3.polyvilleactive.model.transport.ModeTransport;

import java.util.*;
import java.util.stream.Collectors;

/**
 * classe priorite qui attribut un rang sr un mode de transport. Plus le rang est proche de 0, plus il est important
 * -1 est pour eviter
 * @author vivian
 *
 */
public class Priorite {

	public final int rang;
	public final ModeTransport transport;
	
	public Priorite(int rang, ModeTransport transport) {
		this.rang=rang;
		this.transport=transport;
	}

	@Override
	public int hashCode() {
		return Objects.hash(rang,transport);
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) return true;
		if (!(obj instanceof Priorite)) return false;
		Priorite other = (Priorite) obj;
		return this.rang==other.rang
				&& this.transport.equals(other.transport);
	}

	public boolean isAvoid() {
		return rang==-1;
	}
	
	/**
	 * methode qui s'occupe de gerer les conflits des differentes regles qui peuvent arriver en même temps.
	 * @param allPriorities
	 * @return liste des priorite a appliquer
	 */
	public static List<Priorite> compromise(List<Priorite> allPriorities) {
		Set<ModeTransport> transports = new HashSet<>();
		allPriorities.forEach(p -> transports.add(p.transport));

		List<Priorite> selectedPriorities = new ArrayList<>();
		for (ModeTransport transport : transports) {
			List<Priorite> clashingPriorities = allPriorities.stream()
					.filter(p -> p.transport.equals(transport))
					.collect(Collectors.toList());
			if (clashingPriorities.size()==1) {
				// Only one instruction
				selectedPriorities.add(clashingPriorities.get(0));
			} else {
				Optional<Priorite> avoid = clashingPriorities.stream().filter(p -> p.isAvoid()).findFirst();
				List<Priorite> prioritize = clashingPriorities.stream().filter(p -> !p.isAvoid()).collect(Collectors.toList());
				if (avoid.isPresent() && prioritize.isEmpty()) {
					// Only instructed to avoid
					selectedPriorities.add(avoid.get());
				} else if (avoid.isEmpty() && !prioritize.isEmpty()) {
					// Never instructed to avoid
					double sum = prioritize.stream().mapToDouble(p -> p.rang).sum();
					int mean = (int) Math.round( sum / prioritize.size());
					selectedPriorities.add(new Priorite(mean,transport));
				}
			}
		}
		return selectedPriorities;
	}
}
