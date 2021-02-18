package com.aliam3.polyvilleactive.model.incidents;

import com.aliam3.polyvilleactive.dsl.events.Alea;
import com.aliam3.polyvilleactive.dsl.events.transportation.Avance;
import com.aliam3.polyvilleactive.dsl.events.transportation.Panne;
import com.aliam3.polyvilleactive.dsl.events.transportation.Rempli;
import com.aliam3.polyvilleactive.dsl.events.transportation.Retard;
import com.aliam3.polyvilleactive.dsl.events.weather.RainEvent;
import com.aliam3.polyvilleactive.dsl.events.weather.SnowEvent;
import com.aliam3.polyvilleactive.dsl.events.weather.SunEvent;
import com.aliam3.polyvilleactive.model.transport.Journey;
import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.model.transport.Section;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

/**
 * Classe representant un incident reel
 * 
 * @author vivian
 * @author clement
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Incident {

	@JsonIgnore
	protected LocalDateTime timestamp;

	@JsonProperty("line")
	protected String line;

	@JsonProperty("mode_transport")
	protected ModeTransport modeTransport;

	@JsonProperty("num")
	protected String num;

	private Alea alea;

	protected Incident(Alea matchingAlea) {
		this.alea = matchingAlea;
		this.timestamp = LocalDateTime.now();
	}

	protected Incident(Alea matchingAlea, String line, ModeTransport modeTransport, String num) {
		this.line = line;
		this.modeTransport = modeTransport;
		this.num = num;
		this.timestamp = LocalDateTime.now();
		this.alea = matchingAlea;
	}

	protected Incident(Alea matchingAlea, String line, ModeTransport modeTransport) {
		this(matchingAlea, line, modeTransport, null);
	}

	@JsonIgnore
	public String getLine() {
		return line;
	}

	@JsonIgnore
	public ModeTransport getModeTransport() {
		return modeTransport;
	}

	/**
	 * Permet de savoir si l'incident impacte le trajet donne en parametre
	 * 
	 * @param journey
	 * @return true si l'incident impacte le trajet
	 */
	public boolean affects(Journey journey) {
		Section lastSection = null;
		for (Section section : journey.getSections()) {
			if (lastSection == null) {
				lastSection = section;
			}
			if (alea == Alea.REMPLI && !lastSection.isReached() && this.affects(section)) {
				return true;
			}
			if (alea != Alea.REMPLI && !section.isReached() && this.affects(section)) {
				return true;
			}
			if (!section.getTransport().getModeTransport().equals(ModeTransport.WAITING)) {
				lastSection = section;
			}
		}
		return false;
	}

	/**
	 * regarde si l'incident impacte l'etape d'un trajet
	 * 
	 * @param section
	 * @return true si affecte l'étape du trajet
	 */
	public boolean affects(Section section) {
		return section.getTransport().getLine().equals(this.getLine())
				&& section.getTransport().getModeTransport() == this.getModeTransport();
	}

	/**
	 * regarde si l'incident active l'evenement retard de la regle selon la ligne du
	 * transport
	 * 
	 * @param retard
	 * @return true si la regle l'active
	 */
	public boolean triggers(Retard retard) {
		return false;
	}

	/**
	 * regarde si l'incident active l'evenement rempli de la regle selon la ligne du
	 * transport
	 * 
	 * @param rempli
	 * @return true si la regle l'active
	 */
	public boolean triggers(Rempli rempli) {
		return false;
	}

	/**
	 * regarde si l'incident active l'evenement panne de la regle selon la ligne du
	 * transport
	 * 
	 * @param panne
	 * @return true si la regle l'active
	 */
	public boolean triggers(Panne panne) {
		return false;
	}

	/**
	 * regarde si l'incident active l'evenement avance de la regle selon la ligne du
	 * transport
	 * 
	 * @param avance
	 * @return true si la regle l'active
	 */
	public boolean triggers(Avance avance) {
		return false;
	}

	/**
	 * regarde si l'incident active l'evenement neige de la regle selon la ligne du
	 * transport
	 * 
	 * @param snow
	 * @return true si la regle l'active
	 */
	public boolean triggers(SnowEvent snow) {
		return false;
	}

	/**
	 * regarde si l'incident active l'evenement pluie de la regle selon la ligne du
	 * transport
	 * 
	 * @param rain
	 * @return true si la regle l'active
	 */
	public boolean triggers(RainEvent rain) {
		return false;
	}

	/**
	 * regarde si l'incident active l'evenement soleil de la regle selon la ligne du
	 * transport
	 * 
	 * @param sun
	 * @return true si la regle l'active
	 */
	public boolean triggers(SunEvent sun) {
		return false;
	}

	@JsonIgnore
	public boolean isPassed() {
		return false;
	}

	public Alea getAlea() {
		return alea;
	}

	@Override
	public String toString() {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{}";
	}
}
