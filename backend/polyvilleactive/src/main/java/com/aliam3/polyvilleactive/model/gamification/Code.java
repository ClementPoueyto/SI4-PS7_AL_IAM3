package com.aliam3.polyvilleactive.model.gamification;

import java.time.LocalTime;

import com.aliam3.polyvilleactive.model.user.User;

/**
 * Classe qui représente un code promotion
 * @author vivian
 * @author clement
 */
public class Code {
	
	private LocalTime timestamp;
	private User user;
	private Reward reward;
	private String code;
	
	public Code(User user, Reward reward,String code){
		this.timestamp= LocalTime.now();
		this.user= user;
		this.reward = reward;
		this.code=code;
	}
	
	public LocalTime getTimestamp() {
		return timestamp;
	}
	
	public String getCode() {
		return code;
	}
	
	public Reward getReward() {
		return reward;
	}
	
	public User getUser() {
		return user;
	}
}
