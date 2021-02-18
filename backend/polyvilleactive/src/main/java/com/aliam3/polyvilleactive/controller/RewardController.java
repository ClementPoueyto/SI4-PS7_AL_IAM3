package com.aliam3.polyvilleactive.controller;

import com.aliam3.polyvilleactive.model.gamification.Reward;
import com.aliam3.polyvilleactive.model.user.User;
import com.aliam3.polyvilleactive.service.RewardService;
import com.aliam3.polyvilleactive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class RewardController {

    @Autowired
    RewardService rewardService;

    @Autowired
    UserService userService;


    RewardController(){

    }

    /**
     * route utilise pour recuperer les recompenses possibles
     * @return reponse http
     */
    @GetMapping("/rewards")
    @ResponseBody
    public ResponseEntity<List<Reward>> getRewards(){


        List<Reward> rewards = rewardService.getRewards();
        return ResponseEntity.status(200).body(rewards);

    }

    /**
     * route utilise pour utiliser ses points et verifier si on peut effectuer la transaction
     * @param uid
     * @param rid
     * @return http reponse
     */
    @GetMapping("/spendpoints")
    @ResponseBody
    public ResponseEntity<String> selectReward(@RequestParam int uid, @RequestParam int rid){

        Optional<User> user =userService.searchForUser(uid);
        Optional<Reward> reward = rewardService.getRewardById(rid);
        
        if(user.isPresent()&&reward.isPresent()){
            String code = rewardService.generateRewardCode(user.get(),reward.get());
            System.out.println(code);
            if(!code.equals("error")){
                return ResponseEntity.status(HttpStatus.OK).body(code);
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough points");

            }
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no user or no reward found");
        }

    }

}
