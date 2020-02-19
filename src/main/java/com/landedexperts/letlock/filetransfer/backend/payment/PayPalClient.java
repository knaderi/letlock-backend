package com.landedexperts.letlock.filetransfer.backend.payment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

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

public class PayPalClient {
    
    private final Logger logger = LoggerFactory.getLogger(PayPalClient.class);
        
    @Value("${payment.paypal.client.id}")
    private String clientId;
    
    @Value("${payment.paypal.client.secret}")
    private String secretId;
    
    @Value("${payment.paypal.mode}")
    private String mode;
    
    @Value("${payment.paypal.return.link}")
    private String returnLink;
    
    @Value("${payment.paypal.approval.link}")
    private String cancelLink;

  public void initiatePayment(String token, OrderPaymentVO paymentVO) {

      APIContext context = new APIContext(clientId, secretId, mode);
      
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
      payment.setIntent("sale");
      payment.setPayer(payer);
      payment.setRedirectUrls(redirectUrls);
      payment.setTransactions(transactions);
      
      // Create payment
      try {
        Payment createdPayment = payment.create(context);

        Iterator<?> links = createdPayment.getLinks().iterator();
        while (links.hasNext()) {
          Links link = (Links)links.next();
          //TODO: check this.
          if (link.getRel().equalsIgnoreCase("approval_url")) {
            // Redirect the customer to link.getHref()
          }
        }
      } catch (PayPalRESTException e) {
          System.err.println(e.getDetails());
      }
      
      //execute Payment
      payment.setId(paymentVO.getPaymentId());

      PaymentExecution paymentExecution = new PaymentExecution();
      paymentExecution.setPayerId(token);
      try {
        Payment createdPayment = payment.execute(context, paymentExecution);
        logger.debug(createdPayment.toString());
      } catch (PayPalRESTException e) {
        logger.error(e.getDetails().getMessage());
      }
  }
}

