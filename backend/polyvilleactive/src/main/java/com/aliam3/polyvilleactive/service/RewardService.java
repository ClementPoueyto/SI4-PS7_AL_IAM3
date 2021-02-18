package com.aliam3.polyvilleactive.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.aliam3.polyvilleactive.model.gamification.Code;
import com.aliam3.polyvilleactive.model.gamification.Reward;
import com.aliam3.polyvilleactive.model.user.User;

/**
 * Classe qui gere les recompenses
 * 
 * @author vivian
 * @author clement
 */
@Service
public class RewardService {

	/**
	 * liste des codes encore fonctionnels
	 */
	public List<Code> codes;

	/**
	 * listes des récompenses possibles
	 */
	public List<Reward> rewards;

	Random random = new Random();

	private static final String ALPHABET_NOMBRES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public RewardService() {
		codes = new ArrayList<>();
		rewards = new ArrayList<>();
		rewards.add(new Reward(1, "1 titre de transport", 50));
		rewards.add(new Reward(2, "10% de réduction au boucher", 150));
		rewards.add(new Reward(3, "1 place au cinéma", 75));
	}

	/**
	 * genere un code pour la recompense/promotion
	 * 
	 * @return le code genere
	 */
	private String generateCode() {
		int codeLength = 10;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < codeLength; i++) {
			int character = random.nextInt(ALPHABET_NOMBRES.length());
			builder.append(ALPHABET_NOMBRES.charAt(character));
		}

		return builder.toString();

	}

	/**
	 * genere un code temporaire qui est stocke dans une liste pendant 1 heure
	 * 
	 * @param user
	 * @param reward - la recompense
	 * @return
	 */
	public String generateRewardCode(User user, Reward reward) {
		if (reward.getPoints() <= user.getPoints()) {

			user.updatePoint(-reward.getPoints());
			String code = generateCode();
			codes.add(new Code(user, reward, code));
			return code;
		} else {
			return "error";
		}
	}

	/**
	 * regarde si le code est toujours en cours de validite
	 * 
	 * @param code
	 */
	public void checkIfRewardPossible(String code) {
		codes.removeIf(c -> c.getCode().equals(code));
	}

	/**
	 * recuperer une recompense a partir de l'id
	 * 
	 * @param id
	 * @return la recompense
	 */
	public Optional<Reward> getRewardById(int id) {
		for (Reward reward : rewards) {
			if (reward.getId() == id) {
				return Optional.of(reward);
			}
		}
		return Optional.empty();
	}

	/**
	 * enleve les codes generes qui sont en peremption (1h apres la generation du
	 * code)
	 */
	@Scheduled(fixedDelay = 20000)
	public void removeRewardOutdated() {
		for (Code code : codes) {
			if (LocalTime.now().compareTo(code.getTimestamp().plusHours(1)) > 0) {
				code.getUser().updatePoint(code.getReward().getPoints());
				codes.remove(code);
			} else { break; }
		}
	}

	public List<Code> getCodes() {
		return codes;
	}

	public void setCodes(List<Code> codes) {
		this.codes = codes;
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public void setRewards(List<Reward> rewards) {
		this.rewards = rewards;
	}
}
