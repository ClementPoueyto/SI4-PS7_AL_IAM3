package com.aliam3.polyvilleactive.dsl;

import com.aliam3.polyvilleactive.dsl.events.Evenement;
import com.aliam3.polyvilleactive.model.incidents.Incident;

/**
 * Classe qui défini une regle locale. Constitue d'un evenement et d'un action a faire en cas d'activation.
 * @author vivian
 *
 */
public class Regle {
	
	private Evenement event;
	private Action action;
	
	public Regle(Evenement event, Action action) {
		this.event= event;
		this.action= action;
	}
	
	public Evenement getEvent() {
		return event;
	}

	public Action getAction() {
		return action;
	}

	public boolean isAffectedBy(Incident incident) {
		return event.isTriggeredBy(incident);
	}
}
