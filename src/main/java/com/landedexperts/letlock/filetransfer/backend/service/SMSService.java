/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Irina Soboleva - 2020
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.HashMap;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.PublishRequest;

@Service
public class SMSService {
   
    private static final String VERIFICATION_CODE_SMS_TYPE = "Transactional";
    private static final String VERIFICATION_CODE = "%CODE%";
    private static final String VERIFICATION_CODE_MESSAGE = "Your Letlock verification code is " + VERIFICATION_CODE;
    
    private final Logger logger = LoggerFactory.getLogger(SMSService.class);
    
    void sendSMSMessage(String message, String phoneNumber, String smsType) {
        Map<String, MessageAttributeValue> smsAttributes =
            new HashMap<String, MessageAttributeValue>();
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
            .withStringValue(smsType)
            .withDataType("String"));
        AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();
        PublishResult result = snsClient.publish(new PublishRequest()
                        .withMessage(message)
                        .withPhoneNumber(phoneNumber)
                        .withMessageAttributes(smsAttributes));
        logger.info("SMS sent to " + phoneNumber + ". Result is: " + result.toString());
    }

    public void SendVerificationCodeSMS(String code, String phoneNumber) {
        String message = VERIFICATION_CODE_MESSAGE.replace(VERIFICATION_CODE, code);
        sendSMSMessage(message, phoneNumber, VERIFICATION_CODE_SMS_TYPE);
    }

}
