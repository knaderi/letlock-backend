/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.landedexperts.letlock.filetransfer.backend.AWSSecretManagerFacade;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.OrderPaymentVO;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PayPalClient {

    private final Logger logger = LoggerFactory.getLogger(PayPalClient.class);

    private static final String SANDBOX_PAYPAL_MODE = "sandbox";

    private static final String LOCAL_ENV_NAME = "local";

    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";

    @Value("${payment.paypal.mode}")
    private  String mode;

    @Value("${payment.paypal.return.link}")
    private String returnLink;

    @Value("${payment.paypal.cancel.link}")
    private String cancelLink;

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * Initialized to default sandbox client id. will be replaced in
     * loadPayPalProperties() with Live mode one if needed.
     */
    private static String paypalClientId = "ATiJNdLkRuPsxovgGb2WuLIKgge5U9gbffljtwvbBoGOZLzUnAqFawPTO8Qn3jltX2F28f52i5K84Zaa";

    /**
     * Initialized to default sandbox client secret. will be replaced in
     * loadPayPalProperties() with Live mode one if needed.
     */
    private static String paypalClientSecret = "ED3_YwDCnHump5HwL8KLXxXY5Mp_X3ii7-ufXfxk9X_v6OnEGOSb1_GsgKPvHkC4ALY5T9LDDZmGPQtg";

    private static Properties remotePaypalProperties = null;

    public void loadPayPalProperties() {
        if (!isSandBoxEnv() && remotePaypalProperties == null) {
            remotePaypalProperties = AWSSecretManagerFacade.getPayPalProperties(mode);
            paypalClientId = remotePaypalProperties.getProperty(AWSSecretManagerFacade.PAYPAL_CLIENT_ID_KEY);
            paypalClientSecret = remotePaypalProperties.getProperty(AWSSecretManagerFacade.PAYPAL_CLIENT_SECRET_KEY);
        }
    }

    public Map<String, Object> initiatePayment(String token, OrderPaymentVO paymentVO) {
        loadPayPalProperties();
        Map<String, Object> response = new HashMap<String, Object>();
        APIContext context = new APIContext(paypalClientId, paypalClientSecret, mode);

        //////////////////////////////////// Define payment
        // Set payer details
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // Set redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelLink);
        redirectUrls.setReturnUrl(returnLink);

        // Set payment details
        Details details = new Details();
        details.setShipping("0");
        details.setSubtotal(paymentVO.getOrderSubtotal());
        details.setTax(paymentVO.getTaxAmount());

        // Payment amount
        Amount amount = new Amount();
        amount.setCurrency("CAD");
        // Total must be equal to sum of shipping, tax and subtotal.
        amount.setTotal(paymentVO.getOrderTotal());
        amount.setDetails(details);

        // Transaction information
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction
                .setDescription(paymentVO.getOrderDescription());

        // Add transaction to a list
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        // Add payment details
        Payment payment = new Payment();
        payment.setIntent("SALE");
        payment.setPayer(payer);
        payment.setRedirectUrls(redirectUrls);
        payment.setTransactions(transactions);
        Payment createdPayment;

        // Create payment
        try {
            String redirectUrl = "";
            createdPayment = payment.create(context);
            if (createdPayment != null) {
                List<Links> links = createdPayment.getLinks();
                for (Links link : links) {
                    if (link.getRel().equals("approval_url")) {
                        redirectUrl = link.getHref();
                        break;
                    }
                }
                response.put("status", "success");
                response.put("redirect_url", redirectUrl);
            }
        } catch (PayPalRESTException e) {
            logger.error("PayPalClient.initiatePayment!", e);
            response.put("status", "failed");
        }
        return response;
    }

    public Map<String, Object> completePayment(HttpServletRequest req) {
        loadPayPalProperties();
        Map<String, Object> response = new HashMap<String, Object>();
        Payment payment = new Payment();
        payment.setId(req.getParameter("paypalPaymentId"));
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(req.getParameter("paypalPayerId"));
        try {
            APIContext context = new APIContext(paypalClientId, paypalClientSecret, mode);
            Payment createdPayment = payment.execute(context, paymentExecution);
            if (createdPayment != null) {
                response.put("status", "success");
                response.put("payment", createdPayment.toJSON());
            }
        } catch (PayPalRESTException e) {
            logger.error("PayPalClient.completePayment!", e);
            response.put("status", "failed");
        }
        return response;
    }

    /**
     * @return
     */
    private boolean isSandBoxEnv() {
        // The last checks forces to use sandbox when running app locally
        return SANDBOX_PAYPAL_MODE.equals(mode)
                || LOCAL_ENV_NAME.contentEquals(System.getProperty(SPRING_PROFILES_ACTIVE))
                || LOCAL_ENV_NAME.equals(env);
    }

}
