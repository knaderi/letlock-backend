package com.landedexperts.letlock.filetransfer.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.PaymentMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.OrderPaymentVO;
import com.landedexperts.letlock.filetransfer.backend.payment.PayPalClient;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class PaymentController {
    @Autowired
    private PaymentMapper paymentMapper;
    
    @Autowired
    PayPalClient payPalClient;

    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/initiate_payment", produces = { "application/JSON" })
    public ReturnCodeMessageResponse initiatePayment(@RequestParam(value = "token") final String token,
            @RequestParam(value = "orderId") final long orderId) throws Exception {
        logger.info("OrderController.createOrder called for token " + token);

        long userId = SessionManager.getInstance().getUserId(token);
        ReturnCodeMessageResponse response  = new ReturnCodeMessageResponse("SUCCESS", "");
        if (userId > 0) {
            IdVO paymentIDVO = paymentMapper.paymentInitiate(userId, orderId, "paypal");
            long paymentId = paymentIDVO.getResult().getId();
            if (paymentId > 0) {

                OrderPaymentVO paymentVO = paymentMapper.getUserOrderPayment(userId, orderId);
                response  = new ReturnCodeMessageResponse( paymentVO.getReturnCode(), paymentVO.getReturnMessage());
                if (response.getReturnCode().equals("SUCCESS")) {
                    payPalClient.initiatePayment(token, paymentVO);
                }

            } else {
                response  = new ReturnCodeMessageResponse( paymentIDVO.getReturnCode(), paymentIDVO.getReturnMessage());
            }

        } else {
            response =  new ReturnCodeMessageResponse("USER_NOT_FOUND", "user does not exist.");
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/payment_success", produces = { "application/JSON" })
    public ReturnCodeMessageResponse setPaymentSuccess(@RequestParam(value = "token") final String token,
            @RequestParam(value = "pymentId") final long paymentId) throws Exception {
        long userId = SessionManager.getInstance().getUserId(token);
        ReturnCodeMessageResponse response  = new ReturnCodeMessageResponse("SUCCESS", "");

        if (userId > 0) {
            logger.info("OrderController.setPaymentSuccess called for token " + token);
            response = paymentMapper.paymentProcessSuccess(userId, paymentId);
        } else {
            response =  new ReturnCodeMessageResponse("USER_NOT_FOUND", "user does not exist.");
        }
        return response;
    }
}
