package com.aliam3.polyvilleactive.model.user;
import com.aliam3.polyvilleactive.model.gamification.Badge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui represente le profil d'un utilisateur de l'application.
 * On peut retrouver la progression actuelle(gamification) de cette personne ainsi que d'autres informations
 * @author vivian
 *
 */
public class User {
	
    @JsonProperty("id")
    private long id;

    @JsonIgnore
    private UserRole role;

    /**
     * le score global, utilise pour debloquer les badges
     */
    @JsonProperty("score")
    private int score;
    
    /**
     * utilise pour les reductions et autres
     */
    @JsonProperty("points")
    private int points;

    @JsonProperty("badges")
    List<Badge> badges;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    public User(int id, String username, String password, String email){
        this.id=id;
        this.username=username;
        this.password=password;
        this.email=email;
        this.role=UserRole.CITOYEN;
        this.score=0;
        this.points=0;
        this.badges= new ArrayList<>();
    }
    public User(){
        this.score=0;
        this.points=0;
        this.badges= new ArrayList<>();
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    @JsonIgnore
    public boolean isNameEmpty(){
        return username.isEmpty();
    }
    @JsonIgnore
    public boolean isPasswordEmpty(){
        return password.isEmpty();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getPoints() {
		return points;
	}

    public void addBadge(Badge badge){
        if(badge!=null){
            this.badges.add(badge);
        }
    }

    public int getScore() {
        return score;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    /**
     * le nombre que l'on veut ajouter au score actuel
     * @param number
     */
    public void addScore(int number) {
    	score+=number;
    	updatePoint(number);
    }

    public void setBadges(List<Badge> badges) {
        System.out.println(badges);
        this.badges = badges;
    }

    /**
     * le nombre de point que l'on veut enlever ou ajouter
     * @param number
     */
    public void updatePoint(int number) {
    	points+=number;
    }

}
