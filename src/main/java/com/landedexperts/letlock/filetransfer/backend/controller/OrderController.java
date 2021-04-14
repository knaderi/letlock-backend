/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.USER_ID;
import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.FILE_TRANSFER_PRODUCT_NAME;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.OrderMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.CreateOrderResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.OrderFileTransferUsagesResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.OrdersFileTransfersCountsResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.OrdersFileTransfersCountsVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FileTransferOrderLineItemUsageVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

@RestController
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping(value = "/order_create", produces = { "application/JSON" })
    public CreateOrderResponse createOrder(
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        logger.info("OrderController.createOrder called for userId " + userId);
        
        IdVO answer = orderMapper.orderCreate(userId);
        long orderId = answer.getResult().getId();
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();

        return new CreateOrderResponse(orderId, returnCode, returnMessage);
    }

    @PostMapping(value = "/update_order_status_to_cancelled", produces = { "application/JSON" })
    public BooleanResponse updateOrderStatusInitiatedToCancelled(
            @RequestParam(value = "order_id") final String orderId,
            HttpServletRequest request) throws Exception {
        logger.info("OrderController.updateOrderStatusInitiatedToCancelled called for OrderId " + orderId);
        long userId = (long) request.getAttribute(USER_ID);

        ReturnCodeMessageResponse answer = orderMapper.changeStatusInitiatedToCancelled(userId, Integer.parseInt(orderId));
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();
        Boolean result = returnCode.equals("SUCCESS");

        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @PostMapping(value = "/update_order_status_to_initiated", produces = { "application/JSON" })
    public BooleanResponse UpdateOrderStatusCancelledToInitiated(
            @RequestParam(value = "orderId") final int orderId,
            HttpServletRequest request) throws Exception {
        logger.info("OrderController.UpdateOrderStatusCancelledToInitiated called for OrderId " + orderId);
        long userId = (long) request.getAttribute(USER_ID);

        ReturnCodeMessageResponse answer = orderMapper.changeStatusCancelledToInitiated(userId, orderId);
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();
        Boolean result = returnCode.equals("SUCCESS");

        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @PostMapping(value = "/get_packages", produces = { "application/JSON" })
    public JsonResponse<String> getPackages() {
        logger.info("OrderController.getPackages called");
        String value = orderMapper.getPackages(false, false);
        return new JsonResponse<String>(value);
    }

    @PostMapping(value = "/get_locations", produces = { "application/JSON" })
    public JsonResponse<String> getLocations() {
        logger.info("OrderController.getLocations called");
        return new JsonResponse<String>(orderMapper.getLocations());
    }

    @PostMapping(value = "/upsert_order_line_item", produces = { "application/JSON" })
    public JsonResponse<String> upsertOrderLineItem(
            @RequestParam(value = "orderId") final int orderId,
            @RequestParam(value = "packageId") final int packageId,
            @RequestParam(value = "quantity") final short quantity,
            @RequestParam(value = "locationId") final short locationId,
            HttpServletRequest request) throws Exception {
        logger.info("OrderController.upsertOrderLineItem called for OrderId " + orderId);
        long userId = (long) request.getAttribute(USER_ID);
        ReturnCodeMessageResponse answer = orderMapper.upsertOrderLineItem(userId, orderId, packageId, quantity, locationId);
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();
        if (returnCode.equals("SUCCESS")) {
            return getUserOrder(orderId, request);
        }

        return new JsonResponse<String>("", returnCode, returnMessage);
    }

    @PostMapping(value = "/get_user_orders_by_status", produces = { "application/JSON" })
    public JsonResponse<Map<String, String>> getUserOrdersByStatus(
            @RequestParam(value = "orderStatus") final String orderStatus,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        logger.info("OrderController.getUserOrdersByStatus called for userId " + userId + " and status " + orderStatus);

        JsonResponse<Map<String, String>> value = orderMapper.getUserOrders(userId, orderStatus);
        if (null == value) {
            value = new JsonResponse<Map<String, String>>();
        }

        return value;
    }

    @PostMapping(value = "/get_user_orders", produces = { "application/JSON" })
    public JsonResponse<Map<String, String>> getUserOrders(
            HttpServletRequest request) throws Exception {
        return getUserOrdersByStatus("completed", request);
    }

    @PostMapping(value = "/get_user_order", produces = { "application/JSON" })
    public JsonResponse<String> getUserOrder(
            @RequestParam(value = "orderId") final long orderId,
            HttpServletRequest request) throws Exception {
        logger.info("OrderController.getUserOrder called for orderId " + orderId + "\n");
        long userId = (long) request.getAttribute(USER_ID);
        JsonResponse<String> value = orderMapper.getUserOrder(userId, orderId);
        return value;
    }

    @PostMapping(value = "/get_user_ft_order_usage_history", produces = { "application/JSON" })
    public OrderFileTransferUsagesResponse getUserOrderUsageHistroy(
            @RequestParam(value = "orderId") final long orderId,
            HttpServletRequest request) throws Exception {
        logger.info("OrderController.getUserOrderUsageHistroy called for orderId " + orderId + "\n");
        long userId = (long) request.getAttribute(USER_ID);
        
        FileTransferOrderLineItemUsageVO[] lineItemsUsageForOrderArray = orderMapper.getUsersFileTransferOrderUsageHistroy(userId, orderId);
        // TODO, should filter
        OrdersFileTransfersCountsVO ordersFTCounts = orderMapper.getOrdersFileTransferUsageCounts(userId, orderId, FILE_TRANSFER_PRODUCT_NAME);
        if (null == ordersFTCounts) {
            ordersFTCounts = new OrdersFileTransfersCountsVO();
        }
        OrderFileTransferUsagesResponse response = new OrderFileTransferUsagesResponse(orderId, lineItemsUsageForOrderArray, ordersFTCounts);
        return response;
    }

    @PostMapping(value = "/buy_package_now", produces = { "application/JSON" })
    public JsonResponse<String> buyPackageNow(
            @RequestParam(value = "packageId") final int packageId,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        logger.info("OrderController.buyPackageNow called for userId " + userId);
        
        CreateOrderResponse answer = orderMapper.buyPackageNow(userId, packageId);
        Long orderId = answer.getOrderId();
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();
        if (returnCode.equals("SUCCESS")) {
            return getUserOrder(orderId, request);
        }

        return new JsonResponse<String>(orderId.toString(), returnCode, returnMessage);
    }

    @GetMapping(value = "/order/get_filetransfer_order_usage_counts", produces = { "application/JSON" })
    public OrdersFileTransfersCountsResponse getFileTransferUsageCount(
            @RequestParam(value = "orderId") final long orderId,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);

        OrdersFileTransfersCountsVO fileTransferCounts = orderMapper.getOrdersFileTransferUsageCounts(
                userId, orderId, FILE_TRANSFER_PRODUCT_NAME);
        if (null == fileTransferCounts) {
            fileTransferCounts = new OrdersFileTransfersCountsVO(); 
        }
        OrdersFileTransfersCountsResponse value = new OrdersFileTransfersCountsResponse(fileTransferCounts);
        return value;
    }

    @GetMapping(value = "/order/get_filetransfer_usage_counts", produces = { "application/JSON" })
    public OrdersFileTransfersCountsResponse getFileTransferUsageCount(HttpServletRequest request) throws Exception {
        return getFileTransferUsageCount(-1, request);
    }

}
