package com.aliam3.polyvilleactive.stepdefs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.aliam3.polyvilleactive.MemoryRule;
import com.aliam3.polyvilleactive.model.gamification.Reward;
import com.aliam3.polyvilleactive.model.user.User;
import com.aliam3.polyvilleactive.service.JourneyService;
import com.aliam3.polyvilleactive.service.RewardService;
import com.aliam3.polyvilleactive.service.UserService;

import io.cucumber.java8.Fr;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(value = Cucumber.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@CucumberOptions(plugin = {"pretty"}, features = "src/test/resources/features",  glue= {"com.aliam3.polyvilleactive.stepdefs"})
public class RewardStepDefs implements Fr{
	
    @Autowired
    UserService userService;

    @Autowired
    RewardService rewardService;
    @Autowired
    private MockMvc mvc;
    MvcResult result;
    int idReward;
    User u;
    
    public RewardStepDefs() {
    	
    	Etantdonné("Un utilisateur est connecté à l'application mobile avec un id égal à {int}",
                (Integer id) -> {
            userService.users = new ArrayList<>();
            
            u= new User(id, "test", "test", "test@gmail.com");
        });
    	
    	Et("une recompense de {string} coutant {int} points",(String nomRecompense, Integer cout)->{
    		rewardService.rewards.clear();
    		idReward=1000;
    		Reward r= new Reward(idReward, nomRecompense, cout);
    		
    		rewardService.rewards.add(r);
        });
    	
    	Et("possede {int} points",(Integer cout)->{
    		u.addScore(cout);
    		userService.users.add(u);
        });
    	
    	Quand("il veut acheter la reduction {string}", (String nomRecompense)->{
    		result=mvc.perform(get("/spendpoints?uid="+u.getId()+"&rid="+idReward).contentType("application/json")
                    ).andReturn();
        });
    	
    	Alors("il recoit un code", ()->{
    		assertEquals(200, result.getResponse().getStatus());
    		assertEquals(10, result.getResponse().getContentAsString().length());
        });
    	
    	Alors("il recoit une erreur car pas assez de point", ()->{
    		assertEquals(400, result.getResponse().getStatus());
    		assertEquals("Not enough points", result.getResponse().getContentAsString());
        });
    	
    	Alors("son nombre de point est de {int}", (Integer points)->{
    		assertEquals(points, u.getPoints());
        });
    	
    	
    }

}
