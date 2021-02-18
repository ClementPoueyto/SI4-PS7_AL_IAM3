package com.aliam3.polyvilleactive.service;

import com.aliam3.polyvilleactive.model.gamification.Badge;
import com.aliam3.polyvilleactive.model.user.Form;
import com.aliam3.polyvilleactive.model.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe qui gere les utilisateurs
 *
 * @author vivian
 * @author clement
 */
@Service
public class UserService {

	public List<User> users = new ArrayList<>();

	public UserService() {
		User user = new User();
		user.setUsername("test");
		user.setPassword("test");
		user.setEmail("test@gmail.com");
		user.setId(1);
		user.addBadge(new Badge("test", "badge1.png"));
		user.addBadge(new Badge("test3", "badge2.png"));
		User userB = new User();
		userB.setUsername("other");
		userB.setPassword("other");
		userB.setEmail("other@gmail.com");
		userB.setId(2);
		user.addScore(100);
		user.updatePoint(-30);
		user.setBadges(getUnlockedBadges(user.getScore()));
		users.add(user);
		users.add(userB);
	}

	/**
	 * recuperer le profil d'un utilisateur en fonction des infos de son compte
	 *
	 * @param username
	 * @param password
	 * @return user
	 */
	public Optional<User> searchForUser(String username, String password) {
		for (User oneuser : users) {
			if (oneuser.getPassword().equalsIgnoreCase(password) && oneuser.getUsername().equalsIgnoreCase(username)) {
				return Optional.of(oneuser);

			}
		}

		return Optional.empty();
	}

	/**
	 * renvoie l'user id qui deja connecte
	 *
	 * @param id
	 * @return user
	 */
	public Optional<User> searchForUser(long id) {
		for (User oneuser : users) {
			if (oneuser.getId() == id) {
				return Optional.of(oneuser);
			}
		}

		return Optional.empty();
	}

	/**
	 * Vérifie que l'utilisateur est valide et l'inscrit avec un nouvel identifiant
	 *
	 * @param user utilisateur a vérifier
	 * @return l'utilisateur inscit
	 */
	public Optional<User> signupUser(User user) {
		if (user.isNameEmpty() || user.isPasswordEmpty()) {
			return Optional.empty();
		} else {
			user.setId(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
			users.add(user);
			return Optional.of(user);
		}
	}

	/**
	 * renvoie la liste des badges debloques en fonction du score acquis
	 *
	 * @param score
	 * @return liste de badge
	 */
	public List<Badge> getUnlockedBadges(int score) {
		List<Badge> badges = new ArrayList<>();
		if (score >= 10)
			badges.add(new Badge("Badge 'Petite Pousse'", "badge1.png"));
		if (score >= 20)
			badges.add(new Badge("Badge 'Bonne action'", "badge3.png"));
		if (score >= 50)
			badges.add(new Badge("Badge 'Buisson'", "badge1.png"));
		if (score >= 100)
			badges.add(new Badge("Badge 'Bon Citoyen'", "badge4.png"));
		if (score >= 200)
			badges.add(new Badge("Badge 'Foret'", "badge1.png"));
		if (score >= 500)
			badges.add(new Badge("Badge 'Gentillesse Débordante'", "badge2.png"));

		return badges;
	}

	/**
	 * calcule le nombre de point a attribuer en fonction des preferences de
	 * l'utilisateur
	 *
	 * @param form
	 * @return score a attribuer
	 */
	int calculScore(Form form) {
		int point = 25;
		int factor = 1;
		int filtersPoint = 1;
		filtersPoint += form.getFilters().size();

		if (form.isGreen()) {
			factor = 2;
		}

		return (point * factor) / filtersPoint;
	}

	public void updateUser(User user) {
		users.removeIf(c -> c.getId() == (user.getId()));
		users.add(user);
	}

}
