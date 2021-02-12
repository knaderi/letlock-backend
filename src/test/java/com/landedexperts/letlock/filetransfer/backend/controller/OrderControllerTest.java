/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.landedexperts.letlock.filetransfer.backend.TestUtils;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceTypeEnum;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.FileTransferMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.PaymentMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.UserMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FileTransferInfoVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

public class OrderControllerTest extends BaseControllerTest {
    @Autowired
    FileTransferMapper fileTransferMapper;

    @Autowired
    PaymentMapper paymentMapper;


    @Autowired
    UserMapper userMapper;

    String orderId = "0";
    String secondOrderId = "0";
    String orderDetailId = "0";
    String secondOrderDetailId = "0";
    long paymentId = 0;
    List<String> jsonKeyValues = new ArrayList<String>();
    int credits = 0;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        createLoggedInActiveUser();
        login();
    }

    @Test
    public void createOrderTest() throws Exception {
        createOrder();
    }

    private String createOrder() throws Exception, UnsupportedEncodingException {
        String uri = "/order_create";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("createOrderTest", content);
        assertContentForKeyValueLargerThanZero("createOrderTest", content, "orderId");
        orderId = getValuesForGivenKey(content, "orderId");
        return orderId;
    }
    

    public void updateOrderStatusFromInitiatedToCancelled() throws Exception {
        createOrder();
        String uri = "/update_order_status_to_cancelled";
        ResultActions resultAction = mvc.perform(
                MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + token)
                .param("order_id", orderId)
                .accept(MediaType.APPLICATION_JSON_VALUE));
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
                MockMvcRequestBuilders.post(uri)
                .header("Authorization", "Bearer " + token)
                .param("order_id", orderId)
                .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("updateOrderStatusFromCancelledToInitiatedTest", content);
    }

    @Test
    public void createOrderDetailTest() throws Exception {
        createOrder();
        createOrderLineItem(orderId);
        updateOrderLineItem();// updating location to Canada/BC

    }

    @Test
    public void deleteOrderDetailTest() throws Exception {
        createOrder();
        createOrderLineItem(orderId);
        deleteOrderLineItem();// updating location to Canada/BC

    }

    private void createOrderLineItem(String newOrderId) throws Exception, UnsupportedEncodingException {
        if(null == orderId) {
            newOrderId = this.orderId;
        }
        String uri = "/upsert_order_line_item";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .param("orderId", newOrderId)
                        .param("packageId", TEST_PACKAGE_ID)
                        .param("quantity", "1")
                        .param("locationId", "1")
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertHasValueForKey("orderLineItemId", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
        assertForNoError("createOrderDetailTest", content);
    }
    
    private void updateOrderLineItem() throws Exception {
        String uri = "/upsert_order_line_item";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .param("orderId", orderId)
                        .param("packageId", TEST_PACKAGE_ID)
                        .param("quantity", "1")
                        .param("locationId", "3")
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("createOrderDetailTest", content);
        assertHasValueForKey("orderLineItemId", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
        assertHasValueForKey("countryCode", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
        assertHasValueForKey("provinceCode", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
    }

    public void deleteOrderLineItem() throws Exception {
        String uri = "/upsert_order_line_item";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .param("orderId", orderId)
                        .param("packageId", TEST_PACKAGE_ID)
                        .param("quantity", "0") // set the quantity to zero
                        .param("locationId", "1")
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(StringUtils.isEmpty(content));
    }

    @Test
    public void getUserOrdersTest() throws Exception {
        createOrder();
        createOrderLineItem(orderId);
        String content = getUserOrdersContent();
        Assertions.assertTrue(content.length() > 0, "content should exist but value should be empty");
        if (content.contains("\"orderId\" :")) {
            Assertions.assertFalse(content.contains("\"orderId\":" + orderId), "response should not contain initiated orders");
        }
        makeDummySuccessfulPayment(orderId);
        content = getUserOrdersContent();
        String result = JsonResponse.getResult(content).getResult().toString();
        Assertions.assertEquals(orderId,getValuesForGivenKey(result, "orderId", orderId));        
    }
    
    private String getUserOrdersContent() throws Exception {
        String uri = "/get_user_orders";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        return mvcResult.getResponse().getContentAsString();
    }

    @Test
    public void getUsersOrdersForStatusTest() throws Exception {
        createOrder();
        createOrderLineItem(orderId);
        String uri = "/get_user_orders_by_status";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .param("orderStatus", "initiated")
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("getUsersAllOrdersTest", content);

    }

    @Test
    public void getPackagesTest() throws Exception {
        String uri = "/get_packages";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("getProductsTest", content);
        // need to do better assertion than this.
        Assertions.assertTrue(content.contains("productTypeName"), "There should be a product returned");
    }

    @Test
    public void buyPackageNowTest() throws Exception {
        String uri = "/buy_package_now";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .param("packageId", "1")
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("buyPackageNowTest", content);
        assertForNoError("createOrderDetailTest", content);
        assertHasValueForKey("orderLineItemId", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
        assertHasValueForKey("countryCode", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
        assertHasValueForKey("provinceCode", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
    }

    @Test
    public void getUserOrders() throws Exception {
        createOrder();
        createOrderLineItem(orderId);
        String uri = "/get_user_orders";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertHasValueForKey("orderLineItemId", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
        assertHasValueForKey("countryCode", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
        assertHasValueForKey("provinceCode", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
    }

    @Test
    public void getUserOrderTest() throws Exception {
        createOrder();
        createOrderLineItem(orderId);
        String uri = "/get_user_order";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .param("orderId", orderId)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertHasValueForKey("orderLineItemId", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
        assertHasValueForKey("countryCode", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
        assertHasValueForKey("provinceCode", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
    }

    @Test
    public void getLocations() throws Exception {
        String uri = "/get_locations";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertHasValueForKey("countryCode", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
        assertHasValueForKey("provinceCode", JsonResponse.getResult(content).getResult().toString(), jsonKeyValues);
        assertForNoError("getLocationsTest", content);
    }

    
    @Test
    public void getFileTransferSessions() throws Exception {
        // Create test order
        createOrder();
        createOrderLineItem(orderId);
        makeDummySuccessfulPayment(orderId);
        startFileTransfer();
        testGetFileTransferSessions();

    }
    
    private void testGetFileTransferSessions()
            throws Exception, UnsupportedEncodingException {
        // check the usage history
        String uri = "/get_file_transfer_sessions_for_user";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("getFileTransferSessions", content);
    }
    
    @Test
    public void getUserOrderUsages() throws Exception {
        String result = JsonResponse.getResult(getUserOrdersContent()).getResult().toString();
        // Check if the user has free credits added upon registration
        if (result.contains("\"orderId\" :")) {
            // Use the complimentary order for the test
            orderId = getKeyName(result,0); 
            credits = getFreeCreditsCount();
        } else {
            // Create test order
            createOrder();
            createOrderLineItem(orderId);
            makeDummySuccessfulPayment(orderId);
            credits = TRANSFER_CREDITS_FOR_TEST_PACKAGE;
        }
        testFileTransferUsageHistory(credits, credits);
        startFileTransfer();
        testFileTransferUsageHistory(credits-1,credits);
        startFileTransfer();
        testFileTransferUsageHistory(credits-2,credits);
        startFileTransfer();        
        testFileTransferUsageHistory(credits-3,credits);
    }
    
    private int getFreeCreditsCount() throws Exception {
        String uri = "/order/get_filetransfer_order_usage_counts";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.get(uri)
                        .header("Authorization", "Bearer " + token)
                        .param("orderId", orderId)
                        .accept(MediaType.APPLICATION_JSON_VALUE));       
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        return Integer.parseInt(getValuesForGivenKey(content,"originalTransferCounts","result"));
    }

    private void testFileTransferUsageHistory(int availTransferCounts, int originalTransferCounts)
            throws Exception, UnsupportedEncodingException {
        // check the usage history
        String uri = "/get_user_ft_order_usage_history";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .param("orderId", orderId)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        // Assertions.assertEquals(3, TestUtils.getNumberOfRepetitions(content,
        // "\"creditUsed\":1,"));
        Assertions.assertTrue(content.contains(
                "\"availableTransferCounts\":" + availTransferCounts + ",\"originalTransferCounts\":" + originalTransferCounts + ""),"The number of file transfers and avialble ones are not correct.");
        if (availTransferCounts < originalTransferCounts) {
            Assertions.assertTrue(content.contains("\"senderId\":" + userId), "There should be a sender " + content);
        }
    }
    
    @Test
    public void getUserFileTransferCounts() throws Exception {
        String result = JsonResponse.getResult(getUserOrdersContent()).getResult().toString();
        // Check if the user has free credits added upon registration
        if (result.contains("\"orderId\" :")) {
            orderId = getKeyName(result,0);
            credits = getFreeCreditsCount();
        }
        
        // Create test order
        createOrder();
        testFileTransferUsageCounts(credits, credits);
        createOrderLineItem(orderId);
        testFileTransferUsageCounts(credits, credits);
        makeDummySuccessfulPayment(orderId);
        credits = credits + TRANSFER_CREDITS_FOR_TEST_PACKAGE;
        testFileTransferUsageCounts(credits, credits);
        startFileTransfer();
        testFileTransferUsageCounts(credits - 1, credits);
        startFileTransfer();
        testFileTransferUsageCounts(credits - 2, credits);
        startFileTransfer();        
        testFileTransferUsageCounts(credits - 3, credits);
        String newOrderId = createOrder();
        createOrderLineItem(newOrderId);
        makeDummySuccessfulPayment(newOrderId);
        credits = credits + TRANSFER_CREDITS_FOR_TEST_PACKAGE;
        testFileTransferUsageCounts(credits - 3, credits);
    }

    private void testFileTransferUsageCounts(int availTransferCounts, int originalTransferCounts)
            throws Exception, UnsupportedEncodingException {
        // check the usage history
        String uri = "/order/get_filetransfer_usage_counts";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.get(uri)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
       
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains(
                "\"availableTransferCounts\":" + availTransferCounts + ",\"originalTransferCounts\":" + originalTransferCounts + ""),"The number of file transfers and avialble ones are not correct.");
        if (originalTransferCounts < originalTransferCounts) {
            Assertions.assertTrue(content.contains("\"senderId\":" + userId + ","), "There should be a sender " + content);
        }
    }

    private void startFileTransfer() {
        // start a file transfer
        String walletAddressTrimmed = TestUtils.createWalletAddress();
        FileTransferInfoVO vo = fileTransferMapper.insertFileTransferSessionRecord(Long.parseLong(userId),
                walletAddressTrimmed, "llftReceiver", BlockChainGatewayServiceTypeEnum.GOCHAIN_GATEWAY.getValue());
        System.out.println(vo);
    }

    private void makeDummySuccessfulPayment(String newOrderId) {
        IdVO paymentIDVO = paymentMapper.setPaymentInitiate(Long.parseLong(userId), Long.parseLong(newOrderId), "paypal");
        paymentId = paymentIDVO.getResult().getId();
        // do the paypal payment.
        paymentMapper.setPaymentProcessSuccessForTest(Long.parseLong(newOrderId), TestUtils.createPayPalTransactionId());
    }
 

    @Test
    public void initiatePaymentTest() throws Exception {
        createOrder();
        createOrderLineItem(orderId);
        String uri = "/paypal/make/payment";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .param("orderId", orderId)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(!StringUtils.isBlank(content));
        Assertions.assertTrue(getValuesForGivenKey(content, "responseMap", "result").contains("\"status\":\"success\""));
    }

    @Test
    public void CompletePaymentTest() throws Exception {
        String uri = "/paypal/complete/payment";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .param("orderId", orderId + "")
                        .param("paypalToken", "EC-08C0391618037304B")
                        .param("paypalPaymentId", "PAYID-LZG53DA0ST70322U14705837&")
                        .param("paypalPayerId", "TNF9ZSF9L9YL4")
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(!StringUtils.isBlank(content));
        Assertions.assertTrue(getValuesForGivenKey(content, "returnCode").equals("PAYMENT_NOT_FOUND"));
    }


}
