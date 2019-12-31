package com.landedexperts.letlock.filetransfer.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.OrderLineItemMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ErrorCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.OrderLineItemResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class OrderDetailController {
    @Autowired
    private OrderLineItemMapper orderLineItemMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderDetailController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/add_order_line_item", produces = { "application/JSON" })
    public OrderLineItemResponse addOrderLineItem(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_id") final int orderId, @RequestParam(value = "product_id") final int productId,
            @RequestParam(value = "quantity") final short quantity) throws Exception {
        logger.info("OrderDetailController.addOrderDetail called for token " + token + " orderId " + orderId + " productId " + productId);
        int orderLineItemId = -1;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        int userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            IdVO answer = orderLineItemMapper.addOrderLineItem(userId, orderId, productId, quantity);

            orderLineItemId = answer.getId();
            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();
        }

        return new OrderLineItemResponse(orderLineItemId, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update_order_line_item", produces = { "application/JSON" })
    public BooleanResponse updateOrderLineItem(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_line_item_id") final int orderLineItemId, @RequestParam(value = "quantity") final short quantity)
            throws Exception {
        logger.info("OrderDetailController.updateOrderLineItem called for token " + token + " orderDLineItemId " + orderLineItemId + " quantity " + quantity);
        boolean result = false;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        int userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
        	ErrorCodeMessageResponse answer = orderLineItemMapper.updateOrderLineItem(userId, orderLineItemId, quantity);

            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();
            result = errorCode.equals("NO_ERROR");
        }

        return new BooleanResponse(result, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete_order_line_item", produces = { "application/JSON" })
    public BooleanResponse deleteOrderDetail(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_line_item_id") final int orderLineItemId) throws Exception {
        logger.info("OrderDetailController.deleteOrderDetail called for token " + token + " orderDetailId " + orderLineItemId);
        boolean result = false;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        int userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
        	ErrorCodeMessageResponse answer = orderLineItemMapper.deleteOrderLineItem(userId, orderLineItemId);
            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();
            result = errorCode.equals("NO_ERROR");
        }

        return new BooleanResponse(result, errorCode, errorMessage);
    }
}
