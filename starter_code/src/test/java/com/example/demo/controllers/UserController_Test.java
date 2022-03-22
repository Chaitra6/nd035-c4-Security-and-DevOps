package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserController_Test
{
    private UserController userController;

    private UserRepository usrRepos = mock(UserRepository.class);

    private CartRepository cartRepos = mock(CartRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    private static final Logger log = LoggerFactory.getLogger(UserController_Test.class);


    @Before
    public void setUp(){

        userController = new UserController();

        TestUtils.injectObjects(userController, "userRepository", usrRepos);

        TestUtils.injectObjects(userController, "cartRepository", cartRepos);

        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);


        //create new User

    }



// course code along
    @Test
    public void create_user_happy_path() throws Exception {
        log.info("Testing : Creating new User");

        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("testUser");
        req.setPassword("testPassword");
        req.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(req);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User usr = response.getBody();
        assertNotNull(usr);
        assertEquals(0, usr.getId());
        assertEquals("testUser", usr.getUsername());
        assertEquals("thisIsHashed", usr.getPassword());

        log.info("Success User Creation...");

    }

    @Test
    public void create_usr_unsuccessful() {

        log.info(" Testing : Create new user (unsuccessful / unhappy path) ");

        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("testUser");

        // password length < 5
        req.setPassword("pass");
        req.setConfirmPassword("pass");

        final ResponseEntity<User> response = userController.createUser(req);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());


    }

    @Test
    public void find_by_user_id_happy_path(){

        log.info("Testing : Find User by ID ");

        User usr = constructUser();

        // similar to course code along
        when(usrRepos.findById(1l)).thenReturn(Optional.of(usr));

        final ResponseEntity<User> res = userController.findById(1L);

        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());


        assertNotNull(res.getBody());

        assertEquals(usr.getId(), res.getBody().getId());


    }

    @Test
    public void find_by_user_id_unsuccessful(){
        log.info("Testing : Find User by ID ( Unsuccessful / unhappy path");

        // We have not created any User here
        //  Search for ID that doesn't exist
        final ResponseEntity<User> res = userController.findById(1L);

        assertNotNull(res);

        // 404 error status code cause user ID doesn't exist
        assertEquals(404, res.getStatusCodeValue());

    }



    @Test
    public void find_user_by_username_happy_path() {

        log.info(" Testing : Find User by Username ");

        // get the  user
        User usr = constructUser();

        when(usrRepos.findByUsername("chaitra")).thenReturn(usr);

        final ResponseEntity<User> response = userController.findByUserName("chaitra");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User resUser = response.getBody();

        // body should not be null, should have some value
        assertNotNull(response.getBody());
        assertEquals(usr.getUsername(), resUser.getUsername());


    }

    @Test
    public void find_user_by_username_unsuccessful() {

        log.info(" Testing : Find User by Username ( Unsuccessful / Unhappy path\")");

        // We are not creating any User here
        // Searching for username that doesn't exist
        final ResponseEntity<User> response = userController.findByUserName("username");

        assertNotNull(response);

        assertEquals(404, response.getStatusCodeValue());



    }





// Helper method , that creates User
    // This could also b done in Setup()
    public User constructUser(){

        User user = new User();

        user.setId(1L);

        user.setUsername("chaitra");

        user.setPassword("passwordTest");

        return user;
    }
}
