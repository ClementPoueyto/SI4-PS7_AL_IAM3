package com.aliam3.polyvilleactive.dsl;

import com.aliam3.polyvilleactive.exception.SemanticException;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;

import java.util.*;
import java.util.Map.Entry;

/**
 * Classe qui regroupe tout les transport a eviter ou interdire, issu de l'arbre ANTLR
 * @author vivian
 *
 */
public class ClauseVerificator {
	private Map<ModeTransport, Integer> prioritize;
	private List<ModeTransport> avoid;
	private List<ModeTransport> prohibitions;

	Map<ModeTransport, Integer> getPrioritize() {
		return new HashMap<>(prioritize);
	}


	List<ModeTransport> getAvoid() {
		return new ArrayList<>(avoid);
	}


	List<ModeTransport> getProhibitions() {
		return new ArrayList<>(prohibitions);
	}


	public ClauseVerificator() {
		prioritize = new HashMap<>();
		avoid = new ArrayList<>();
		prohibitions = new ArrayList<>();
	}

	/**
	 * methode qui ajoute un transport dans la liste adequate, selon le token donne en parametre
	 * @param token
	 * @param modeTransport
	 * @param rang
	 */
	public void priorite(String token, ModeTransport modeTransport, int rang) {
		switch (token) {
		case "PRIORISER":
			if (rang < 0)
				throw new SemanticException("Le niveau de priorité d'un transport doit être positif ou nul");

			if (prioritize.containsKey(modeTransport)) {
				throw new SemanticException(
						"Le transport " + modeTransport + " ne peut pas avoir plusieurs niveaux de priorité");
			} else {
				prioritize.put(modeTransport, rang);
			}
			break;
		case "EVITER":
			avoid.add(modeTransport);
			break;
		default:
			throw new IllegalArgumentException("Given token is not recognized");
		}
		
			
			
	}

	public void interdire(ModeTransport modeTransport) {
		prohibitions.add(modeTransport);
	}

	/**
	 * verifie s'il n'y a pas de transport qui se chevauchent dans les differentes listes
	 */
	public void verify() {
		Set<ModeTransport> modeTransports = prioritize.keySet();

		for (ModeTransport t : modeTransports) {
			if (avoid.contains(t)) {
				throw new SemanticException("Impossible de prioriser et d'éviter le même transport (" + t + ")");
			}
			if (prohibitions.contains(t)) {
				throw new SemanticException("Impossible de prioriser et d'interdire le même transport (" + t + ")");
			}
		}
		for(ModeTransport t : avoid) {
			if (prohibitions.contains(t)) {
				throw new SemanticException("Impossible d'éviter et d'interdire le même transport (" + t + ")");
			}
		}

		// Different transports with same rank
		Set<Integer> ranks = new HashSet<>();
		for (ModeTransport t : modeTransports) {
			int currRank = prioritize.get(t);
			if (!ranks.add(currRank))
				throw new SemanticException("Il y a plus d'un moyen de transport avec pour priorité " + currRank);
		}
	}
	
	/**
	 * cree les priorites des transport, prend en compte ceux de la liste prioriser et la liste eviter
	 * @return
	 */
	public List<Priorite> createPriorities() {
		List<Priorite> p = new ArrayList<>();
		for (ModeTransport transp : avoid) {
			p.add(new Priorite(-1, transp));
		}
		for (Entry<ModeTransport, Integer> transp : prioritize.entrySet()) {
			p.add(new Priorite(transp.getValue(), transp.getKey()));
		}
		return p;

	}

	public void resetList() {
		prohibitions.clear();
		avoid.clear();
		prioritize.clear();

	}

	public List<Prohibition> createProhibitions() {
		List<Prohibition> p = new ArrayList<>();
		for (ModeTransport transp : prohibitions) {
			p.add(new Prohibition(transp));
		}
		return p;

	}

}
