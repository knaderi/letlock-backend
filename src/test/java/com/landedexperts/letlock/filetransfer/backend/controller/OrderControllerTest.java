package com.landedexperts.letlock.filetransfer.backend.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.landedexperts.letlock.filetransfer.backend.TestUtils;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.FileTransferMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.PaymentMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.OrdersInfoResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FileTransferInfoVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

public class OrderControllerTest extends BaseControllerTest {
    @Autowired
    FileTransferMapper fileTransferMapper;

    @Autowired
    PaymentMapper paymentMapper;
    
    @Autowired
    private ObjectMapper objectMapper;

    String orderId = "0";
    String orderDetailId = "0";
    long paymentId = 0;

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
        createOrder();
        createOrderLineItem();
        updateOrderLineItem();//updating location to Canada/BC

    }

    @Test
    public void deleteOrderDetailTest() throws Exception {
        createOrder();
        createOrderLineItem();
        deleteOrderLineItem();//updating location to Canada/BC

    }
    
    private void createOrderLineItem() throws Exception, UnsupportedEncodingException {
        String uri = "/upsert_order_line_item";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("orderId", orderId).param("packageId", "3")
                        .param("quantity", "1").param("locationId", "1").accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        OrdersInfoResponse response = objectMapper.readValue(content, OrdersInfoResponse.class);
        assertForNoError("createOrderDetailTest", content);
        assertTrue(response != null);
        assertTrue(response.getResult() != null);
        assertTrue(response.getResult().length>0);        
        orderDetailId = String.valueOf(response.getResult()[0].getOrderLineItemId());
        assertTrue(!orderDetailId.equals("0"));
    }

    private void updateOrderLineItem() throws Exception {
        String uri = "/upsert_order_line_item";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("orderId", orderId).param("packageId", "3")
                        .param("quantity", "1").param("locationId", "3").accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("createOrderDetailTest", content);
        assertTrue(content.contains("\"orderLineItemId\":"));
        assertTrue(content.contains("\"countryCode\":\"CA\""));
        assertTrue(content.contains("\"provinceCode\":\"BC\""));
    }

    public void deleteOrderLineItem() throws Exception {
        String uri = "/upsert_order_line_item";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("orderId", orderId).param("packageId", "3")
                        .param("quantity", "0").param("locationId", "1").accept(MediaType.APPLICATION_JSON_VALUE));//set the quantity to zero
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("\"result\":[]"));
    }

    @Test
    public void getUserOrdersTest() throws Exception {
        createOrder();
        createOrderLineItem();
        String uri = "/get_user_orders";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("orderId", "-1").param("userStatus", "active")
                        .param("orderStatus", "initiated").accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("getUserOrdersTest", content);
        // need to do better assertion than this.
        // assertTrue("There should be a product returned",
        // content.contains("productName"));
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
        // need to do better assertion than this.
        assertTrue("There should be a product returned", content.contains("productTypeName"));
    }

    @Test
    public void buyPackageNowTest() throws Exception {
        String uri = "/buy_package_now";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("package_id", "1")
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("buyPackageNowTest", content);
        // need to do better assertion than this.
        assertTrue("There should be a new order returned", content.contains("orderId"));
    }

    @Test
    public void getUserOrders() throws Exception {
        createOrder();
        createOrderLineItem();
        String uri = "/get_user_orders";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("orderId", orderId).param("userStatus", "active")
                        .param("orderStatus", "initiated").accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        // TODO: complete test
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
        // need to do better assertion than this.
        assertTrue("There should be a product returned", content.contains("countryName"));
        // TODO: complete test
    }


    @Test
    public void getUserOrderUsages() throws Exception {
        //Create test order
        createOrder();
        createOrderLineItem();
        makeSuccessfulPayment();
        startFileTransfer();
        startFileTransfer();
        startFileTransfer();
        
        //check the usage history
        String uri = "/get_user_ft_order_usage_history";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("orderId", orderId).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(3, TestUtils.getNumberOfRepetitions(content, "\"creditUsed\":1,"));        
        assertTrue("There should be a sender", content.contains("\"availableTransferCounts\":17,\"originalTransferCounts\":20"));
        assertTrue("There should be a sender", content.contains("\"senderId\":" + userId + ","));
    }
    
//    @Test
//    public void getAllUserOrderUsages() throws Exception {
//        //Create test order
//        createOrder();
//        createOrderLineItem();        
//        makeSuccessfulPayment();
//        //start two file transfers
//        startFileTransfer();
//        startFileTransfer();
//        
//        //check the usage history.
//        String uri = "/get_all_user_ft_orders_usage_history";
//        ResultActions resultAction = mvc
//                .perform(MockMvcRequestBuilders.post(uri).param("token", token).accept(MediaType.APPLICATION_JSON_VALUE));
//        resultAction.andExpect(ok);
//        MvcResult mvcResult = resultAction.andReturn();
//        String content = mvcResult.getResponse().getContentAsString();
//        assertEquals(2, TestUtils.getNumberOfRepetitions(content, "\"creditUsed\":1,"));
//        assertTrue("There should be a sender", content.contains("\"senderId\":" + userId + ","));
//      
//    }

    private void startFileTransfer() {
        //start a file transfer
        String walletAddressTrimmed = TestUtils.createWalletAddress();
        FileTransferInfoVO vo = fileTransferMapper.insertFileTransferSessionRecord(Long.parseLong(userId),
                walletAddressTrimmed, "Bob1111");
        System.out.println(vo);
    }

    private void makeSuccessfulPayment() {
        IdVO paymentIDVO = paymentMapper.paymentInitiate(Long.parseLong(userId), Long.parseLong(orderId), "paypal", TestUtils.createPayPalTransactionId());
        paymentId = paymentIDVO.getId();
        paymentMapper.paymentProcessSuccess(Long.parseLong(userId), paymentId);
    }


}
