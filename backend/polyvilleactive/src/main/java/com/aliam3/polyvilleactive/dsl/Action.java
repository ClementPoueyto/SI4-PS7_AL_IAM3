package com.aliam3.polyvilleactive.dsl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * classe  qui definit la liste des transport a utiliser et a ne pas prendre
 * @author vivian
 *
 */
public class Action {

	private List<Priorite> priorites;
	private List<Prohibition> prohibitions;
	
	public Action() {
		this.priorites= new ArrayList<>();
		this.prohibitions= new ArrayList<>();
	}

	public Action(List<Priorite> priorites, List<Prohibition> prohibitions) {
		this.priorites= new ArrayList<>(priorites);
		this.prohibitions= new ArrayList<>(prohibitions);
	}
	
	public List<Priorite> getPriorites() {
		return new ArrayList<>(priorites);
	}

	public List<Prohibition> getProhibitions() {
		return new ArrayList<>(prohibitions);
	}

	public void setPriorities(List<Priorite> priorite) {
		priorites.clear();
		priorites.addAll(priorite);
	}

	@Override
	public int hashCode() {
		return Objects.hash(priorites);
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) return true;
		if (!(obj instanceof Action)) return false;
		Action other = (Action) obj;
		return this.priorites.equals(other.priorites);
	}

	public void setProhibitions(List<Prohibition> prohibition) {
		prohibitions.clear();
		prohibitions.addAll(prohibition);
		
	}
}
