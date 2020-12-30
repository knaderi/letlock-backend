/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Irina Soboleva - 2020
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.ContactMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ContactResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class ContactController {
    @Autowired
    private ContactMapper contactMapper;
    private final Logger logger = LoggerFactory.getLogger(ContactController.class);
    
    @PostMapping(value = "/contacts", produces = { "application/JSON" })
    public BooleanResponse createContact(@RequestParam(value = "token") final String token,
            @RequestParam(value = "contactUserName") final String contactUserName,
            @RequestParam(value = "contactLabel") final String contactLabel
            ) throws Exception {

        logger.info("ContactController.createContact called for token " + token + "\n");

        Boolean result = false;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            ReturnCodeMessageResponse response = contactMapper.createContact(userId, contactUserName, contactLabel);
            returnCode = response.getReturnCode();
            returnMessage = response.getReturnMessage();
        }
        result = returnCode.equals("SUCCESS");
        return new BooleanResponse(result, returnCode, returnMessage);
    }
    
    
    @GetMapping(value = "contacts", produces = { "application/JSON" })
    public ContactResponse getUserContacts(@RequestParam(value = "token") final String token) throws Exception {
 
        logger.info("ContactController.getUserContacts called for token " + token + "\n");

        ContactResponse result = new ContactResponse();

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            result = contactMapper.getUserContacts(userId);
        } else {
            result.setReturnCode("TOKEN_INVALID");
            result.setReturnMessage("Invalid token");
        }
        if (null == result) {
            result = new ContactResponse();
        }
        return result;
    }
    
    
    @PutMapping(value = "/contacts", produces = { "application/JSON" })
    public BooleanResponse updateContact(@RequestParam(value = "token") final String token,
            @RequestParam(value = "contactUserName") final String contactUserName,
            @RequestParam(value = "contactLabel") final String contactLabel
            ) throws Exception {

        logger.info("ContactController.updateContact called for token " + token + "\n");
        
        Boolean result = false;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            ReturnCodeMessageResponse response = contactMapper.updateContact(userId, contactUserName, contactLabel, false);
            returnCode = response.getReturnCode();
            returnMessage = response.getReturnMessage();
        }
        result = returnCode.equals("SUCCESS");
        return new BooleanResponse(result, returnCode, returnMessage);
    }
    
    @DeleteMapping(value = "/contacts", produces = { "application/JSON" })
    public BooleanResponse deleteContact(@RequestParam(value = "token") final String token,
            @RequestParam(value = "contactUserName") final String contactUserName
            ) throws Exception {

        logger.info("ContactController.deleteContact called for token " + token + "\n");
        
        Boolean result = false;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            ReturnCodeMessageResponse response = contactMapper.updateContact(userId, contactUserName, "", true);
            returnCode = response.getReturnCode();
            returnMessage = response.getReturnMessage();
        }
        result = returnCode.equals("SUCCESS");
        return new BooleanResponse(result, returnCode, returnMessage);
    }
    
}
