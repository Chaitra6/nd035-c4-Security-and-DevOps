package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    // Get Item controller
    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private static final Logger log = LoggerFactory.getLogger(ItemControllerTest.class);




    @Before
    public void setUp(){


        itemController = new ItemController();

        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);


        //creating new item
        // could also be done as separate helper function
        Item item = new Item();

        item.setId(2L);

        item.setName("item_name");

        item.setPrice(new BigDecimal(250));

        item.setDescription("This is test item test item");

        when(itemRepository.findAll()).thenReturn(singletonList(item));

        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));

        when(itemRepository.findByName("item_name")).thenReturn(singletonList(item));

    }


    @Test
    public void getAll_items_happy_path(){

        log.info("Testing : Get all items from Repository ");

        ResponseEntity<List<Item>> res = itemController.getItems();

        assertNotNull(res);

        assertEquals(200, res.getStatusCodeValue());

        // 1 item is added, thus size of the array list is 1
        assertEquals(1, res.getBody().size());
    }


    @Test
    public void get_item_from_ItemName(){

        log.info("Testing : Get items by itemName ");

        ResponseEntity<List<Item>> res = itemController.getItemsByName("item_name");

        assertNotNull(res);

        assertEquals(200, res.getStatusCodeValue());

        // 1 item is added, thus size of the  list is 1
        // this means we have got the item with Item name
        assertEquals(1, res.getBody().size());
    }

    @Test
    public void get_item_from_ItemName_Unsuccessful(){

        log.info("Testing : Get items by itemName ( Unhappy Path)");

        // Getting Item name that Doesn't exist
        ResponseEntity<List<Item>> res = itemController.getItemsByName("name_not_present");

        assertNotNull(res);

        assertEquals(404, res.getStatusCodeValue());


    }


    @Test
    public void get_item_by_itemId_happy_path(){

        log.info("Testing : Get item by ID ");

        ResponseEntity<Item> res = itemController.getItemById(2L);

        assertNotNull(res);

        assertEquals(200, res.getStatusCodeValue());

        // check if you have got the item, thus response should be not null
        assertNotNull(res.getBody());

    }

    @Test
    public void get_item_by_itemId_Unsuccessful(){

        log.info("Testing : Get item by ID ( Unsuccessful / Unhappy Path) ");

        // Trying to get non-existent ID
        ResponseEntity<Item> res = itemController.getItemById(1L);

        assertNotNull(res);

        assertEquals(404, res.getStatusCodeValue());



    }


}
