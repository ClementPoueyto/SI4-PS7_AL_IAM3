package com.aliam3.polyvilleactive.dsl.events;

import com.aliam3.polyvilleactive.model.incidents.Incident;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;

/**
 * Classe qui decrit un evenement obtenu après l'envoi des IOT
 * @author vivia
 *
 */
public abstract class Evenement {

	private ModeTransport transport;
	private Alea alea;
	
	protected Evenement(ModeTransport transp, Alea alea) {
		this.transport=transp;
		this.alea=alea;
	}

	public abstract boolean isTriggeredBy(Incident incident);

	public ModeTransport getModeTransport() {
		return transport;
	}

	public Alea getAlea() {
		return alea;
	}

}
