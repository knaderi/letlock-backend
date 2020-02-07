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
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.AddOrderLineItemResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class OrderDetailController {
    @Autowired
    private OrderLineItemMapper orderLineItemMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderDetailController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/add_order_line_item", produces = { "application/JSON" })
    public AddOrderLineItemResponse addOrderLineItem(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_id") final int orderId, @RequestParam(value = "product_id") final int productId,
            @RequestParam(value = "quantity") final short quantity) throws Exception {
        logger.info("OrderDetailController.addOrderDetail called for token " + token + " orderId " + orderId + " productId " + productId);
        long orderLineItemId = -1;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            IdVO answer = orderLineItemMapper.addOrderLineItem(userId, orderId, productId, quantity);

            orderLineItemId = answer.getId();
            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();
        }

        return new AddOrderLineItemResponse(orderLineItemId, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update_order_line_item", produces = { "application/JSON" })
    public BooleanResponse updateOrderLineItem(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_line_item_id") final int orderLineItemId, @RequestParam(value = "quantity") final short quantity)
            throws Exception {
        logger.info("OrderDetailController.updateOrderLineItem called for token " + token + " orderDLineItemId " + orderLineItemId + " quantity " + quantity);
        boolean result = false;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
        	ReturnCodeMessageResponse answer = orderLineItemMapper.updateOrderLineItem(userId, orderLineItemId, quantity);

            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();
            result = returnCode.equals("SUCCESS");
        }

        return new BooleanResponse(result, returnCode, returnMessage);
    }

}
