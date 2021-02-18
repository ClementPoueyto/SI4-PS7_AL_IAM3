package com.aliam3.polyvilleactive.service;
import static org.junit.jupiter.api.Assertions.*;

import com.aliam3.polyvilleactive.model.transport.ModeTransport;
import com.aliam3.polyvilleactive.model.user.Form;
import com.aliam3.polyvilleactive.model.user.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @BeforeEach
    public void setUp() {
        userService.users = new ArrayList<>();
    }

    @Test
    public void searchForUserTest(){

        User user1 = new User();
        user1.setUsername("test");
        user1.setPassword("test");
        user1.setId(1);
        userService.updateUser(user1);
        Optional<User> user = userService.searchForUser("test","test");
        assertTrue(user.isPresent());
        assertEquals(1, user.get().getId());

        userService.users= new ArrayList<>();
        Optional<User> userUnknown = userService.searchForUser("test2","test2");
        assertFalse(userUnknown.isPresent());

        userService.users= new ArrayList<>();
        Optional<User> user2 = userService.searchForUser("test","test");
        assertFalse(user2.isPresent());
    }

    @Test
    public void signUpTest() {
    	User u = new User();
    	u.setPassword("");
    	u.setUsername("test");
    	assertEquals(Optional.empty(), userService.signupUser(u));
    	assertEquals(0, userService.users.size());
    	u.setPassword("test");
    	assertEquals(Optional.of(u), userService.signupUser(u));
    	assertEquals(1, userService.users.size());
    }
    
    @Test
    public void calculeScoreTest(){
        Form form = new Form();
        form.setFilters(new ArrayList<>());
        form.setGreen(true);

        assertEquals(50, userService.calculScore(form));

        List<ModeTransport> transports = new ArrayList<>();
        transports.add(ModeTransport.TRAIN);
        form.setFilters(transports);
        assertEquals(25, userService.calculScore(form));

        transports.add(ModeTransport.METRO);
        form.setFilters(transports);
        assertEquals(16, userService.calculScore(form));

        transports.add(ModeTransport.BUS);
        form.setFilters(transports);
        assertEquals(12, userService.calculScore(form));


        form.setGreen(false);
        transports = new ArrayList<>();
        form.setFilters(transports);
        assertEquals(25, userService.calculScore(form));

        transports.add(ModeTransport.TRAIN);
        form.setFilters(transports);
        assertEquals(12, userService.calculScore(form));

        transports.add(ModeTransport.METRO);
        form.setFilters(transports);
        assertEquals(8, userService.calculScore(form));

        transports.add(ModeTransport.BUS);
        form.setFilters(transports);
        assertEquals(6, userService.calculScore(form));

    }
    
    @Test
    public void getUnlockedBadgesTest() {
        assertEquals(1,userService.getUnlockedBadges(10).size());
        assertEquals(2,userService.getUnlockedBadges(20).size());
        assertEquals(0,userService.getUnlockedBadges(3).size());
        assertEquals(6,userService.getUnlockedBadges(751).size());
    }
}
