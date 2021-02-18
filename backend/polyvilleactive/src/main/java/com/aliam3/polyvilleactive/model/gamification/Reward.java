package com.aliam3.polyvilleactive.model.gamification;

import java.util.Objects;

/**
 * Classe qui représente une recompense/promotions
 * @author vivian
 * @author clement
 */
public class Reward {

	private String title;
	private int points;
	private int id;

	public Reward(int id,String title, int points) {
		this.id = id;
		this.title = title;
		this.points = points;

	}

	public String getTitle() {
		return title;
	}

	public int getPoints() {
		return points;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(points,title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Reward))
			return false;
		Reward other = (Reward) obj;
		return points == other.points && title.equals(other.title);
	}


	
	
}
