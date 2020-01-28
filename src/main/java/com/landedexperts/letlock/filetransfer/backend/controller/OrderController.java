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
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.OrderResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
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
        long orderId = -1;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            IdVO answer = orderMapper.orderCreate(userId);

            orderId = answer.getId();
            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();
        }

        return new OrderResponse(orderId, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update_order_status_to_cancelled", produces = { "application/JSON" })
    public BooleanResponse updateOrderStatusInitiatedToCancelled(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_id") final String orderId) throws Exception {
        logger.info("OrderController.updateOrderStatusInitiatedToCancelled called for token " + token + " and OrderId " + orderId);
        Boolean result = false;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
        	ReturnCodeMessageResponse answer = orderMapper.changeStatusInitiatedToCancelled(userId, Integer.parseInt(orderId));

            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();

            result = returnCode.equals("SUCCESS");
        }

        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update_order_status_to_initiated", produces = { "application/JSON" })
    public BooleanResponse UpdateOrderStatusCancelledToInitiated(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_id") final int orderId) throws Exception {
        logger.info("OrderController.UpdateOrderStatusCancelledToInitiated called for token " + token + " and OrderId " + orderId);
        Boolean result = false;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
        	ReturnCodeMessageResponse answer = orderMapper.changeStatusCancelledToInitiated(userId, orderId);

            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();

            result = returnCode.equals("SUCCESS");
        }

        return new BooleanResponse(result, returnCode, returnMessage);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/get_packages", produces = { "application/JSON" })
    public JsonResponse getPackages(String token) {
        logger.info("OrderController.getProducts called for token " + token);
        JsonResponse value = null;
        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            value = orderMapper.getActiveProducts(false, false);
        }

        return value;
    }
    
   
    
    @RequestMapping(method = RequestMethod.POST, value = "/upsert_order_line_item", produces = { "application/JSON" })
    public BooleanResponse upsertOrderLineItem(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_id") final int orderId,
            @RequestParam(value = "package_id") final int packageId,
            @RequestParam(value = "quantity") final short quantity,
            @RequestParam(value = "quantity") final short locationId) throws Exception {
        logger.info("OrderController.upsertOrderLineItem called for token " + token + " and OrderId " + orderId);
        Boolean result = false;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            ReturnCodeMessageResponse answer = orderMapper.upsertOrderLineItem(userId, orderId, packageId, quantity,locationId);

            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();

            result = returnCode.equals("SUCCESS");
        }

        return new BooleanResponse(result, returnCode, returnMessage);
    }
}
