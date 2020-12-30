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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.TwoFAMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class TwoFAController {
    private final Logger logger = LoggerFactory.getLogger(TwoFAController.class);
    
    @Autowired
    EmailServiceFacade emailServiceFacade;
    @Autowired
    private TwoFAMapper twoFAMapper;
    
    @RequestMapping(method = RequestMethod.POST, value = "/2fa/update_settings", produces = { "application/JSON" })
    public BooleanResponse update2FASettings(@RequestParam(value = "token") final String token,
            @RequestParam(value = "password") final String password,
            @RequestParam(value = "email") final String email,
            @RequestParam(value = "enabled") final Boolean enabled,
            @RequestParam(value = "phoneNumber") final String phoneNumber) {
        logger.info("TwoFAController.update2FASettings called for email " + email);
        long userId = SessionManager.getInstance().getUserId(token);
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";
        boolean result = false;
        try {
            if (userId > 0) {
                ReturnCodeMessageResponse response = twoFAMapper.update2FASettings(userId, password, email, enabled, phoneNumber);
                returnCode = response.getReturnCode();
                returnMessage = response.getReturnMessage();
                if ("SUCCESS".equals(returnCode)) {
                    result = true;
                } else {
                    logger.error("TwoFAController.update2FASettings for email " + email
                            + " failed. returnCode: " + returnCode
                            + " returnMessage: " + returnMessage);
                }
            }
        } catch (Exception e) {
            returnCode = "UPDATE_2FA_SETTINGS_ERROR";
            returnMessage = e.getMessage();
            logger.error("TwoFAController.update2FASettings for email " + email
                    + " failed. returnCode: " + returnCode
                    + " returnMessage: " + returnMessage);
        }
        return new BooleanResponse(result, returnCode, returnMessage);
    }
    

    
}
