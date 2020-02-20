package com.landedexperts.letlock.filetransfer.backend.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

@Component
public class PayPalClient {
    
    private final Logger logger = LoggerFactory.getLogger(PayPalClient.class);
        
    @Value("${payment.paypal.client.id}")
    private String clientId;
    
    @Value("${payment.paypal.client.secret}")
    private String clientSecret;
    
    @Value("${payment.paypal.mode}")
    private String mode;
    
    @Value("${payment.paypal.return.link}")
    private String returnLink;
    
    @Value("${payment.paypal.cancel.link}")
    private String cancelLink;

  public Map<String, Object> initiatePayment(String token, OrderPaymentVO paymentVO) {
      Map<String, Object> response = new HashMap<String, Object>();
      APIContext context = new APIContext(clientId, clientSecret, mode);
      
      ////////////////////////////////////Define payment
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
          if(createdPayment!=null){
              List<Links> links = createdPayment.getLinks();
              for (Links link:links) {
                  if(link.getRel().equals("approval_url")){
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
  
  
  public Map<String, Object> completePayment(HttpServletRequest req){
      Map<String, Object> response = new HashMap<String, Object>();
      Payment payment = new Payment();
      payment.setId(req.getParameter("paymentId"));
      PaymentExecution paymentExecution = new PaymentExecution();
      paymentExecution.setPayerId(req.getParameter("payerId"));
      try {
          APIContext context = new APIContext(clientId, clientSecret, "sandbox");
          Payment createdPayment = payment.execute(context, paymentExecution);
          if(createdPayment!=null){
              response.put("status", "success");
              response.put("payment", createdPayment);
          }
      } catch (PayPalRESTException e) {
          logger.error("PayPalClient.completePayment!", e);
          response.put("status", "failed");
      }
      return response;
  }

}

