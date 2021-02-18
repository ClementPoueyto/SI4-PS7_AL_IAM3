package com.aliam3.polyvilleactive.service;

import com.aliam3.polyvilleactive.model.gamification.Badge;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.aliam3.polyvilleactive.model.gamification.Reward;
import com.aliam3.polyvilleactive.model.user.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Optional;

@SpringBootTest
public class RewardServiceTest {

	@Autowired
	RewardService rewardService;

	@Test
	public void generateRewardCodeTest() {
		User u= new User();
		String code=rewardService.generateRewardCode(u, new Reward(0,"1 titre de transport", 50));
		assertTrue(code.equals("error"));
		u.addScore(100);
		code=rewardService.generateRewardCode(u, new Reward(0,"1 titre de transport", 50));
		assertTrue(code.length()==10);
		assertTrue(rewardService.codes.size()==1);
		
	}
	
	@Test
	public void checkIfRewardPossible() {
		User u= new User();
		u.addScore(100);
		String code=rewardService.generateRewardCode(u, new Reward(0,"1 titre de transport", 50));
		rewardService.checkIfRewardPossible(code);
		assertTrue(u.getPoints()==50);
		
	}

	@Test
	public void getRewardByIdTest(){
		rewardService.rewards.add(new Reward(32,"test", 700));
		Optional<Reward> reward = rewardService.getRewardById(32);
		Assertions.assertTrue(reward.isPresent());
		Assertions.assertEquals(32, reward.get().getId());
		Assertions.assertEquals("test", reward.get().getTitle());
		Assertions.assertEquals(700, reward.get().getPoints());
		Assertions.assertEquals(new Reward(32,"test", 700),reward.get());
		Assertions.assertNotEquals(new Reward(33,"test", 800),reward.get());

	}



}
