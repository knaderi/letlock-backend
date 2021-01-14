/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Irina Soboleva - 2020
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.TwoFAMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.SessionTokenResponse;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;
import com.landedexperts.letlock.filetransfer.backend.session.TwoFAManager;
import com.landedexperts.letlock.filetransfer.backend.session.VerificationResult;
import com.landedexperts.letlock.filetransfer.backend.utils.RequestData;

@RestController
public class TwoFAController {
    private final Logger logger = LoggerFactory.getLogger(TwoFAController.class);
    
    @Autowired
    EmailServiceFacade emailServiceFacade;
    @Autowired
    private TwoFAMapper twoFAMapper;
    
    @PutMapping(value = "/2fa_settings", produces = { "application/JSON" })
    public BooleanResponse update2FASettings(@RequestParam(value = "token") final String token,
            @RequestParam(value = "password") final String password,
            @RequestParam(value = "email") final String email,
            @RequestParam(value = "enabled") final Boolean enabled,
            @RequestParam(value = "phoneNumber") final String phoneNumber,
            HttpServletRequest httpServletRequest) {
        logger.info("TwoFAController.update2FASettings called for email " + email);
        long userId = SessionManager.getInstance().getUserId(token);
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";
        boolean result = false;
        try {
            if (userId > 0) {
                RequestData requestData = new RequestData(httpServletRequest.getRemoteAddr(), 
                        httpServletRequest.getHeader("origin"), 
                        httpServletRequest.getHeader("User-Agent"));
                ReturnCodeMessageResponse response = twoFAMapper.update2FASettings(userId, password, email, enabled, phoneNumber, requestData.toJSON());
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
    
    @GetMapping(value = "/2fa_login_code", produces = { "application/JSON" })
    public ReturnCodeMessageResponse send2FALoginCode(@RequestParam(value = "token") final String token,
            @RequestParam(value = "sendTo") final String sendTo,
            @RequestParam(value = "type") final String type // [email | text]
            ) {
        logger.info("TwoFAController.send2FALoginCode called for token " + token);
        long userId = TwoFAManager.getInstance().getUserId(token);
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";
        if (userId > 0) {
            try {
                if ("email".equals(type)) {
                    ReturnCodeMessageResponse response = twoFAMapper.verifyEmail(userId, sendTo);
                    returnCode = response.getReturnCode();
                    returnMessage = response.getReturnMessage();
                    if ("SUCCESS".equals(returnCode)) {
                        emailServiceFacade.sendVerificationCode(sendTo, TwoFAManager.getInstance().generateCode(token));
                    }
                } else {
                    returnCode = "WRONG_ARGUMENT";
                    returnMessage = "Wrong value for parameter 'type'";
                }
            } catch (Exception e) {
                returnCode = "SEND_VERIFICATION_CODE_ERROR";
                returnMessage = e.getMessage();
            }
        }
        
        return new ReturnCodeMessageResponse(returnCode, returnMessage);
    }

    @PutMapping(value = "/2fa_login_code", produces = { "application/JSON" })
    public SessionTokenResponse verify2FALoginCode(@RequestParam(value = "token") final String token,
            @RequestParam(value = "code") final String code,
            HttpServletRequest httpServletRequest
            ) {
        logger.info("TwoFAController.verify2FALoginCode called for token " + token);
        long userId = TwoFAManager.getInstance().getUserId(token);
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";
        String new_token = "";
        if (userId > 0) {
            VerificationResult result = TwoFAManager.getInstance().verifyCode(token, code);
            if (result.getValid()) {
                RequestData requestData = new RequestData(httpServletRequest.getRemoteAddr(), 
                        httpServletRequest.getHeader("origin"), 
                        httpServletRequest.getHeader("User-Agent"));
                new_token = SessionManager.getInstance().generateSessionToken(userId, requestData.getTokenPrefix());                 
                returnCode = "SUCCESS";
                returnMessage = "";
            } else if (result.getAttempts() < 3) {
                returnCode = "CODE_INVALID";
                returnMessage = "Verification code invalid";
            } else {
                returnCode = "TOO_MANY_ATTEMPTS";
                returnMessage = "Wrong verification code was entered more than 3 times";
            }
        }

        return new SessionTokenResponse(new_token, returnCode, returnMessage);
    }
}
