/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.PaymentMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.CompletePayPalPaymentResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.InitiatePaypalPaymentResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.OrderPaymentVO;
import com.landedexperts.letlock.filetransfer.backend.payment.PayPalClient;

@RestController
@RequestMapping(value = "/paypal")
public class PayPalController extends BaseController{
    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    PayPalClient payPalClient;

    private final Logger logger = LoggerFactory.getLogger(PayPalController.class);

    @PostMapping(value = "/make/payment", produces = { "application/JSON" })
    public InitiatePaypalPaymentResponse initiatePayment(
            @RequestParam(value = "orderId") final long orderId,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute("user.id");
        logger.info("PayPalController.initiatePayment called for userId " + userId);
        
        IdVO paymentIDVO = paymentMapper.setPaymentInitiate(userId, orderId, "paypal");
        long paymentId = paymentIDVO.getResult().getId();
        InitiatePaypalPaymentResponse response = new InitiatePaypalPaymentResponse(paymentIDVO.getReturnCode(), paymentIDVO.getReturnMessage());
        if (paymentId > 0) {
            OrderPaymentVO paymentVO = paymentMapper.getUserOrderPayment(userId, orderId);
            if (!paymentVO.isValid()) {
                return new InitiatePaypalPaymentResponse("PAMENT_INFO_NOT_VALID", "payment info retrived from db is not valid.");
            }
            response = new InitiatePaypalPaymentResponse(paymentVO.getReturnCode(), paymentVO.getReturnMessage());
            if (paymentVO.getReturnCode().equals("SUCCESS")) {
                Map<String, Object> initiationMap = payPalClient.initiatePayment(getToken(request), paymentVO);
                if (isSuccess(initiationMap)) {
                    response.setResponseMap(initiationMap);
                } else {
                    paymentMapper.setPaymentProcessFailure(userId, orderId, "");//set failure only if paypal api fails.
                    response = new InitiatePaypalPaymentResponse("PAYPAL_INITIATE_PAYMENT_FAILED", "Paypal initiate payment failed.");
                }
            }
        }
        return response;
    }

    @PostMapping(value = "/complete/payment", produces = { "application/JSON" })
    public CompletePayPalPaymentResponse completePayPalPayment(
            @RequestParam(value = "orderId") final long orderId,
            @RequestParam(value = "paypalToken") final String paypalToken,
            @RequestParam(value = "paypalPayerId") final String paypalPayerId,
            @RequestParam(value = "paypalPaymentId") final String paypalPaymentId,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute("user.id");
        logger.info("PayPalController.setPaymentSuccess called for orderId " + orderId);
        CompletePayPalPaymentResponse response = paymentMapper.setPaymentProcessSuccess(userId, orderId, paypalPaymentId);
        if (response.getReturnCode().equals("SUCCESS")) {
            Map<String, Object> completeMap = payPalClient.completePayment(request);
            if (isSuccess(completeMap)) {
                response.setResponseMap(completeMap);
            } else {
                paymentMapper.setPaymentProcessFailure(userId, orderId, "-1");//set failure only if paypal api fails.
                response = new CompletePayPalPaymentResponse("PAYPAL_PAYMENT_FAILED", "Paypal  complete payment failed.");
            }
        }
        //TODO: log the value of Payment Json object to the database upon successful payment.
        return response;
    }
    
    @GetMapping(value = "/cancel", produces = { "application/JSON" })
    public BooleanResponse handleCancelPayment() throws Exception {
        return new BooleanResponse(true, "Success", "");
    }

    @GetMapping(value = "/return", produces = { "application/JSON" })
    public BooleanResponse handleReturnLink() throws Exception {
        return new BooleanResponse(true, "Success", "");
    }

    
    private boolean isSuccess(Map<String, Object> map) {
        return null !=  map &&  map.size() > 0 && map.get("status").equals("success");
    }
    
 
}
