/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Irina Soboleva - 2020
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.ContactMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ContactResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import static com.landedexperts.letlock.filetransfer.backend.utils.BackendConstants.USER_ID;

@RestController
@RequestMapping(value = "/contacts", produces = { "application/JSON" })
public class ContactController {
    @Autowired
    private ContactMapper contactMapper;
    
    @PostMapping
    public BooleanResponse createContact(
            @RequestParam(value = "contactUserName") final String contactUserName,
            @RequestParam(value = "contactLabel") final String contactLabel,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        ReturnCodeMessageResponse response = contactMapper.createContact(userId, contactUserName, contactLabel);
        String returnCode = response.getReturnCode();
        return new BooleanResponse(returnCode.equals("SUCCESS"), returnCode, response.getReturnMessage());
    }
    
    
    @GetMapping
    public ContactResponse getUserContacts(HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        return contactMapper.getUserContacts(userId);
    }
    
    
    @PutMapping
    public BooleanResponse updateContact(
            @RequestParam(value = "contactUserName") final String contactUserName,
            @RequestParam(value = "contactLabel") final String contactLabel,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        ReturnCodeMessageResponse response = contactMapper.updateContact(userId, contactUserName, contactLabel, false);
        String returnCode = response.getReturnCode();
        return new BooleanResponse(returnCode.equals("SUCCESS"), returnCode, response.getReturnMessage());
    }
    
    @DeleteMapping
    public BooleanResponse deleteContact(
            @RequestParam(value = "contactUserName") final String contactUserName,
            HttpServletRequest request) throws Exception {
        
        long userId = (long) request.getAttribute(USER_ID);
        ReturnCodeMessageResponse response = contactMapper.updateContact(userId, contactUserName, "", true);
        String returnCode = response.getReturnCode();
        return new BooleanResponse(returnCode.equals("SUCCESS"), returnCode, response.getReturnMessage());
    }
    
}
