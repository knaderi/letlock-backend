package com.landedexperts.letlock.filetransfer.backend.controller;

import java.io.UnsupportedEncodingException;

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
        updateOrderStatusFromInitiatedToCancelledTest();
        updateOrderStatusFromCancelledToInitiatedTest();
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

    private void updateOrderStatusFromInitiatedToCancelledTest() throws Exception {
        String uri = "/update_order_status_to_cancelled";
        ResultActions resultAction = mvc.perform(
                MockMvcRequestBuilders.post(uri).param("token", token).param("order_id", orderId).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("updateOrderStatusFromInitiatedToCancelledTest", content);
    }

    
    private void updateOrderStatusFromCancelledToInitiatedTest() throws Exception {
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
        createOrder();
        createOrderDetail();
        updateOrderDetail();
        
    }

    private void createOrderDetail() throws Exception, UnsupportedEncodingException {
        String uri = "/add_order_detail";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("order_id", orderId).param("product_id", "3").param("quantity", "1").accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("createOrderDetailTest", content);
        assertContentForKeyValueLargerThanZero("createOrderTest", content, "orderDetailId");
        orderDetailId = getValuesForGivenKey(content, "orderDetailId");
    }
    
    

    private void updateOrderDetail() throws Exception {
        String uri = "/update_order_detail";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("order_detail_id", orderDetailId).param("quantity", "2").accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("createOrderDetailTest", content);
    }
    
    @Test
    public void deleteOrderDetailTest() throws Exception {
        createOrder();
        createOrderDetail();
        String uri = "/delete_order_detail";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("order_detail_id", orderDetailId).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("deleteOrderDetailTest", content);
    }
}
