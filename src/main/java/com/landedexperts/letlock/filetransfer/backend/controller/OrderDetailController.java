package com.landedexperts.letlock.filetransfer.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mapper.OrderDetailMapper;
import com.landedexperts.letlock.filetransfer.backend.database.vo.ErrorCodeMessageVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.response.OrderDetailResponse;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class OrderDetailController {
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderDetailController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/add_order_detail", produces = { "application/JSON" })
    public OrderDetailResponse addOrderDetail(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_id") final int orderId, @RequestParam(value = "product_id") final int productId,
            @RequestParam(value = "quantity") final short quantity) throws Exception {
        logger.info("OrderDetailController.addOrderDetail called for token " + token + " orderId " + orderId + " productId " + productId);
        int orderDetailId = -1;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        int userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            IdVO answer = orderDetailMapper.addOrderDetail(userId, orderId, productId, quantity);

            orderDetailId = answer.getId();
            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();
        }

        return new OrderDetailResponse(orderDetailId, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update_order_detail", produces = { "application/JSON" })
    public BooleanResponse updateOrderDetail(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_detail_id") final int orderDetailId, @RequestParam(value = "quantity") final short quantity)
            throws Exception {
        logger.info("OrderDetailController.updateOrderDetail called for token " + token + " orderDetailId " + orderDetailId + " quantity " + quantity);
        boolean result = false;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        int userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            ErrorCodeMessageVO answer = orderDetailMapper.updateOrderDetail(userId, orderDetailId, quantity);

            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();
            result = errorCode.equals("NO_ERROR");
        }

        return new BooleanResponse(result, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete_order_detail", produces = { "application/JSON" })
    public BooleanResponse deleteOrderDetail(@RequestParam(value = "token") final String token,
            @RequestParam(value = "order_detail_id") final int orderDetailId) throws Exception {
        logger.info("OrderDetailController.deleteOrderDetail called for token " + token + " orderDetailId " + orderDetailId);
        boolean result = false;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        int userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            ErrorCodeMessageVO answer = orderDetailMapper.deleteOrderDetail(userId, orderDetailId);

            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();
            result = errorCode.equals("NO_ERROR");
        }

        return new BooleanResponse(result, errorCode, errorMessage);
    }
}
