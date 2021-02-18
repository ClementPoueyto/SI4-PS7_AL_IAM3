package com.aliam3.polyvilleactive.model.transport;

import java.time.Duration;
import java.util.Objects;

/**
 * Classe qui definit un moyen de transport utilise dans le trajet, avec toute
 * les informations: le nom de sa ligne, le temps passe dans ce transport...
 * 
 * @author vivian
 * @author clement
 */
public class Transport {
	private ModeTransport modeTransport;
	private Duration duration;
	private String numero;
	private String line;

	public Transport() {
	}

	public Transport(ModeTransport modeTransport, Duration dur) {
		this.modeTransport = modeTransport;
		this.duration = dur;
	}

	public Transport(ModeTransport modeTransport, Duration dur, String numero, String line) {
		this(modeTransport, dur);
		this.line = line;
		this.numero = numero;

	}

	public ModeTransport getModeTransport() {
		return modeTransport;
	}

	public void setModeTransport(ModeTransport modeTransport) {
		this.modeTransport = modeTransport;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	@Override
	public int hashCode() {
		return Objects.hash(modeTransport, numero, line);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Transport))
			return false;
		Transport other = (Transport) obj;
		return this.modeTransport.equals(other.modeTransport) && this.line.equals(other.line)
				&& this.numero.equals(other.numero);
	}
}
