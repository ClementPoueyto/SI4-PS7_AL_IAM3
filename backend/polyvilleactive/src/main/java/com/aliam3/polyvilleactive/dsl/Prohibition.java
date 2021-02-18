package com.aliam3.polyvilleactive.dsl;

import com.aliam3.polyvilleactive.model.transport.ModeTransport;

import java.util.*;

/**
 * Classe qui s'occupe des transport a eviter
 * @author vivian
 *
 */
public class Prohibition {

	public final ModeTransport transport;
	
	public Prohibition(ModeTransport transport) {
		this.transport=transport;
	}

	@Override
	public int hashCode() {
		return Objects.hash(transport);
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) return true;
		if (!(obj instanceof Prohibition)) return false;
		Prohibition other = (Prohibition) obj;
		return this.transport.equals(other.transport);
	}

	
}
