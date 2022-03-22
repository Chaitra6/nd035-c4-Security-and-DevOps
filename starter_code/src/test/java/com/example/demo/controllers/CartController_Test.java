package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartController_Test {

    // get Cart Controller
    private CartController cartController;

    private final UserRepository usrRepo = mock(UserRepository.class);

    private final CartRepository cartRepos = mock(CartRepository.class);

    private final ItemRepository itemRepos = mock(ItemRepository.class);

    //get logger to log information
    private static final Logger log = LoggerFactory.getLogger(CartController_Test.class);


    @Before
    public void setUp(){

        cartController = new CartController();

        TestUtils.injectObjects(cartController, "userRepository", usrRepo);

        TestUtils.injectObjects(cartController, "cartRepository", cartRepos);

        TestUtils.injectObjects(cartController, "itemRepository", itemRepos);


    }

    // Helper Functions
    // This could also b done in Setup()


    // Construct new User
    public User constructUser(){

        User user = new User();

        Cart cart = new Cart();

        user.setId(1L);

        user.setUsername("chaitra");

        user.setPassword("passwordTest");

        user.setCart(cart);

        return user;
    }




    //Construct New Item
    public Item constructItem(){
        //get new item
        Item item = new Item();

        item.setId(2L);

        item.setName("item_name");

        item.setPrice(new BigDecimal(250));

        item.setDescription("This is test item test item");

        return item;
    }



    // Edit Cart
    public ModifyCartRequest modify_Cart(){

        ModifyCartRequest modCartReq = new ModifyCartRequest();

        modCartReq.setUsername("chaitra");

        modCartReq.setItemId(2l);

        modCartReq.setQuantity(5);

        return modCartReq;
    }




    @Test
    public void add_to_Cart_happy_path() {

        log.info("Testing : Add item to cart ");

        // create new user
        User user = constructUser();

        //new cart is already created in - constructUser()
        Cart cart = user.getCart();

        cart.setUser(user);

        // create new item
        Item item = constructItem();


        //create new ModifyCartRequest object
        ModifyCartRequest modifyCartReq = modify_Cart();


        //find user and item and return if found
        when(usrRepo.findByUsername("chaitra")).thenReturn(user);

        when(itemRepos.findById(2L)).thenReturn(Optional.of(item));


        ResponseEntity<Cart> res = cartController.addTocart(modifyCartReq);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());

        Cart cartMod = res.getBody();
        assertNotNull(cartMod);
        assertTrue(cartMod.getItems().contains(item));

    }


    @Test
    public void add_to_Cart_Unsuccessful() {

        log.info("Testing : Add item to cart (Unsuccessful / Unhappy Path ");

        //Not creating a User


        // create new item
        Item item = constructItem();


        //create new ModifyCartRequest object
        ModifyCartRequest modifyCartReq = modify_Cart();


        //find item and return if found
        when(itemRepos.findById(2L)).thenReturn(Optional.of(item));


        ResponseEntity<Cart> res = cartController.addTocart(modifyCartReq);
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());



    }



    @Test
    public void remove_from_cart_happy_path(){
        log.info("Testing : Remove item from cart ");


        // create user
        User user = constructUser();

        // new cart instance is already created in constructUser
        Cart cart = user.getCart();

        //set cart user
        cart.setUser(user);

        // create item
        Item item = constructItem();

        //create new ModifyCartRequest object
        ModifyCartRequest modifyCartReq = modify_Cart();

        //find user and item and return if found
        when(usrRepo.findByUsername("chaitra")).thenReturn(user);
        when(itemRepos.findById(2L)).thenReturn(Optional.of(item));


        //remove the cart
        ResponseEntity<Cart> res = cartController.removeFromcart(modifyCartReq);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());


        Cart resCart = res.getBody();

        assertNotNull(resCart);
        assertFalse(resCart.getItems().contains(item));
    }



    @Test
    public void remove_from_cart_Unsuccessful(){
        log.info("Testing : Remove item from cart  ( Unsuccessful / Unhappy Path ");


        // Not creating User


        // create item
        Item item = constructItem();

        //create new ModifyCartRequest object
        ModifyCartRequest modifyCartReq = modify_Cart();

        //find item and return if found
//        when(usrRepo.findByUsername("chaitra")).thenReturn(user);
        when(itemRepos.findById(2L)).thenReturn(Optional.of(item));


        //remove the cart
        ResponseEntity<Cart> res = cartController.removeFromcart(modifyCartReq);
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());



    }

}
