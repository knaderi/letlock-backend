package com.landedexperts.letlock.filetransfer.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.OrderMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ErrorCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.OrderResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/order_create", produces = { "application/JSON" })
    public OrderResponse createOrder(@RequestParam(value = "token") final String token) throws Exception {
        logger.info("OrderController.createOrder called for token " + token);
        int orderId = -1;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            IdVO answer = orderMapper.orderCreate(userId);

            orderId = answer.getId();
            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();
        }

        return new OrderResponse(orderId, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update_order_status_to_cancelled", produces = { "application/JSON" })
    public BooleanResponse updateOrderStatusInitiatedToCancelled(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_id") final int orderId) throws Exception {
        logger.info("OrderController.updateOrderStatusInitiatedToCancelled called for token " + token + " and OrderId " + orderId);
        Boolean result = false;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        Long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
        	ErrorCodeMessageResponse answer = orderMapper.changeStatusInitiatedToCancelled(userId, orderId);

            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();

            result = errorCode.equals("NO_ERROR");
        }

        return new BooleanResponse(result, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update_order_status_to_initiated", produces = { "application/JSON" })
    public BooleanResponse UpdateOrderStatusCancelledToInitiated(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_id") final int orderId) throws Exception {
        logger.info("OrderController.UpdateOrderStatusCancelledToInitiated called for token " + token + " and OrderId " + orderId);
        Boolean result = false;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        Long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
        	ErrorCodeMessageResponse answer = orderMapper.changeStatusCancelledToInitiated(userId, orderId);

            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();

            result = errorCode.equals("NO_ERROR");
        }

        return new BooleanResponse(result, errorCode, errorMessage);
    }
}
