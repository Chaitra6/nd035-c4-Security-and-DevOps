package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderController_Test {


    private OrderController orderController;

    private OrderRepository orderRepos = mock(OrderRepository.class);

    private UserRepository userRepos = mock(UserRepository.class);

    //get logger to log information
    private static final Logger log = LoggerFactory.getLogger(CartController_Test.class);


    @Before
    public void setUp(){

        orderController = new OrderController();

        TestUtils.injectObjects(orderController, "userRepository", userRepos);

        TestUtils.injectObjects(orderController, "orderRepository", orderRepos);

    }


    // Helper Functions
    // This could also b done in Setup()


    // Construct new User
    public User constructUser(){

        User user = new User();

//        Cart cart = new Cart();

        user.setId(1L);

        user.setUsername("chaitra");

        user.setPassword("passwordTest");


        return user;
    }




    //Construct New Item
    public List<Item> constructItem(){
        //get new item
        Item item = new Item();

        item.setId(1L);

        item.setName("item_name");

        item.setPrice(new BigDecimal(550));

        item.setDescription("This is test item test item");

        List<Item> items = new ArrayList<Item>();

        items.add(item);

        return items;
    }


    public Cart constructCart(User user){

        Cart cart = new Cart();

        cart.setUser(user);

        // create item and set in cart item list
        List<Item> items = constructItem();

        cart.setItems(items);

        cart.setTotal(BigDecimal.valueOf(550));

        return cart;
    }






    @Test
    public void order_submit_happy_path(){

        log.info("Testing : Order Submission");

        // create user
        User user = constructUser();

        // create cart
        Cart cart = constructCart(user);

        // set the user cart
        user.setCart(cart);

        when(userRepos.findByUsername("chaitra")).thenReturn(user);

        ResponseEntity<UserOrder> res = orderController.submit("chaitra");

        assertNotNull(res);

        assertEquals(200, res.getStatusCodeValue());

        BigDecimal total = res.getBody().getTotal();

        // cart total of username: chaitra should be 550
        assertEquals(BigDecimal.valueOf(550), Objects.requireNonNull(total));

    }


    @Test
    public void order_User_not_found(){
        log.info("Testing : Order Submission , User Not Found");

        //submit for non-existent username
        ResponseEntity<UserOrder> res = orderController.submit("username_none");

        assertNotNull(res);

        assertEquals(404, res.getStatusCodeValue());

    }

    @Test
    public void get_User_orders_happy_path(){

        log.info(" Testing : Get all User Orders ");

        //create user
        User usr = constructUser();


        when(userRepos.findByUsername("chaitra")).thenReturn(usr);

        List<UserOrder> orders = new ArrayList<>();

        // get orders for particular user
        ResponseEntity<List<UserOrder>> res = orderController.getOrdersForUser("chaitra");
        assertNotNull(res);

        assertEquals(200, res.getStatusCodeValue());
        orders = res.getBody();
        assertNotNull(orders);

    }

    @Test
    public void get_User_orders_Unsuccessful(){

        log.info(" Testing : Get all User Orders (Unsuccessful / Unhappy Path)");

        //getting orders for non-existent username

        List<UserOrder> orders = new ArrayList<>();

        // get orders for particular user
        ResponseEntity<List<UserOrder>> res = orderController.getOrdersForUser("username_none");
        assertNotNull(res);

        assertEquals(404, res.getStatusCodeValue());



    }

}



