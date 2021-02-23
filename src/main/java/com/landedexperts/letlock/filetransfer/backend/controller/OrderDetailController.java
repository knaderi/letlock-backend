/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.USER_ID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.OrderLineItemMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.AddOrderLineItemResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

@RestController
public class OrderDetailController {
    @Autowired
    private OrderLineItemMapper orderLineItemMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderDetailController.class);

    @PostMapping(value = "/add_order_line_item", produces = { "application/JSON" })
    public AddOrderLineItemResponse addOrderLineItem(
            @RequestParam(value = "order_id") final int orderId,
            @RequestParam(value = "product_id") final int productId,
            @RequestParam(value = "quantity") final short quantity,
            HttpServletRequest request) throws Exception {
        logger.info("OrderDetailController.addOrderDetail called for orderId " + orderId + " productId " + productId);
        long userId = (long) request.getAttribute(USER_ID);

        IdVO answer = orderLineItemMapper.addOrderLineItem(userId, orderId, productId, quantity);
        long orderLineItemId = answer.getResult().getId();
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();
        
        return new AddOrderLineItemResponse(orderLineItemId, returnCode, returnMessage);
    }

    @PostMapping(value = "/update_order_line_item", produces = { "application/JSON" })
    public BooleanResponse updateOrderLineItem(
            @RequestParam(value = "order_line_item_id") final int orderLineItemId,
            @RequestParam(value = "quantity") final short quantity,
            HttpServletRequest request) throws Exception {
        logger.info("OrderDetailController.updateOrderLineItem called for orderLineItemId " + orderLineItemId + " quantity " + quantity);
        long userId = (long) request.getAttribute(USER_ID);

        ReturnCodeMessageResponse answer = orderLineItemMapper.updateOrderLineItem(userId, orderLineItemId, quantity);
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();

        return new BooleanResponse(returnCode.equals("SUCCESS"), returnCode, returnMessage);
    }

}
