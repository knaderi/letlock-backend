package com.landedexperts.letlock.filetransfer.backend.controller;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

public class OrderControllerTest extends BaseControllerTest {

    String orderId = "0";
    String orderDetailId = "0";

    @Override
    @Before
    @Transactional
    public void setUp() throws Exception {
        super.setUp();
        registerUser();
        login();
    }

    @Test
    public void createOrderTest() throws Exception {
        createOrder();
    }

    private void createOrder() throws Exception, UnsupportedEncodingException {
        String uri = "/order_create";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("createOrderTest", content);
        assertContentForKeyValueLargerThanZero("createOrderTest", content, "orderId");
        orderId = getValuesForGivenKey(content, "orderId");
    }


    public void updateOrderStatusFromInitiatedToCancelled() throws Exception {
        createOrder();
        String uri = "/update_order_status_to_cancelled";
        ResultActions resultAction = mvc.perform(
                MockMvcRequestBuilders.post(uri).param("token", token).param("order_id", orderId).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("updateOrderStatusFromInitiatedToCancelledTest", content);
    }


    public void updateOrderStatusFromCancelledToInitiatedTest() throws Exception {
        createOrder();
        updateOrderStatusFromInitiatedToCancelled();
        String uri = "/update_order_status_to_initiated";
        ResultActions resultAction = mvc.perform(
                MockMvcRequestBuilders.post(uri).param("token", token).param("order_id", orderId).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("updateOrderStatusFromCancelledToInitiatedTest", content);
    }

    @Test
    public void createOrderDetailTest() throws Exception {
//        createOrder();
//        createOrderLineItem();
//        updateOrderLineItem();
        
    }

    private void createOrderLineItem() throws Exception, UnsupportedEncodingException {
        String uri = "/upsert_order_line_item";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("order_id", orderId).param("package_id", "3").param("quantity", "1").param("location_id", "1").accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("createOrderDetailTest", content);
       // assertContentForKeyValueLargerThanZero("createOrderTest", content, "orderLineItemId");
       // orderDetailId = getValuesForGivenKey(content, "orderLineItemId");
    }
    
    

    private void updateOrderLineItem() throws Exception {
        String uri = "/update_order_line_item";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("order_line_item_id", orderDetailId).param("quantity", "2").accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("createOrderLineItemTest", content);
    }
    
    @Test
    public void deleteOrderDetailTest() throws Exception {
//        createOrder();
//        createOrderLineItem();
//        String uri = "/delete_order_detail";
//        ResultActions resultAction = mvc
//                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("order_line_item_id", orderDetailId).accept(MediaType.APPLICATION_JSON_VALUE));
//        resultAction.andExpect(ok);
//        MvcResult mvcResult = resultAction.andReturn();
//        String content = mvcResult.getResponse().getContentAsString();
//        assertForNoError("deleteOrderDetailTest", content);
    }//TODO: fix this
    

    @Test
    public void getUserOrdersTest() throws Exception {
        createOrder();
        createOrderLineItem();
        String uri = "/get_user_orders";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("getUserOrdersTest", content);
        //need to do better assertion than this.
      //  assertTrue("There should be a product returned", content.contains("productName"));
    }
    
    @Test
    public void getPackagesTest() throws Exception {
        String uri = "/get_packages";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("getProductsTest", content);
        //need to do better assertion than this.
        assertTrue("There should be a product returned", content.contains("productTypeName"));
    } 
    
    @Test
    public void getUserOrders() throws Exception {
        createOrder();
        createOrderLineItem();
        String uri = "/get_user_orders";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("userId", userId).param("orderId", "1").param("userStatus", "active").param("orderStatus", "initiated").accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        //TODO: complete test
    }
    
    @Test 
    public void getLocations() throws Exception {        
        String uri = "/get_locations";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("getLocationsTest", content);
        //need to do better assertion than this.
        assertTrue("There should be a product returned", content.contains("countryName"));
        //TODO: complete test
    }
}
