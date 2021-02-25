/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Irina Soboleva - 2021
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

import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.USER_ID;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.TwoFAMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.LoginResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;
import com.landedexperts.letlock.filetransfer.backend.session.TwoFAManager;
import com.landedexperts.letlock.filetransfer.backend.utils.RequestData;
import com.landedexperts.letlock.filetransfer.backend.utils.ResponseCode;
import com.landedexperts.letlock.filetransfer.backend.utils.Verify2FACodeResult;

@RestController
public class TwoFAController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(TwoFAController.class);
    
    @Autowired
    EmailServiceFacade emailServiceFacade;
    @Autowired
    private TwoFAMapper twoFAMapper;
    
    @PutMapping(value = "/2fa_settings", produces = { "application/JSON" })
    public BooleanResponse update2FASettings(
            @RequestParam(value = "password") final String password,
            @RequestParam(value = "email") final String email,
            @RequestParam(value = "enabled") final Boolean enabled,
            @RequestParam(value = "phoneNumber") final String phoneNumber,
            HttpServletRequest request) throws Exception {
        logger.info("TwoFAController.update2FASettings called for email " + email);
        long userId = (long) request.getAttribute(USER_ID);

        RequestData requestData = new RequestData(request);
        ReturnCodeMessageResponse response = twoFAMapper.update2FASettings(
                userId, password, email, enabled, phoneNumber, requestData.toJSON());
        String returnCode = response.getReturnCode();
        String returnMessage = response.getReturnMessage();

        return new BooleanResponse("SUCCESS".equals(returnCode), returnCode, returnMessage);
    }
    
    @GetMapping(value = "/2fa_login_code", produces = { "application/JSON" })
    public ReturnCodeMessageResponse send2FALoginCode(
            @RequestParam(value = "sendTo") final String sendTo,
            @RequestParam(value = "type") final String type, // [email | text]
            HttpServletRequest request) {
        logger.info("TwoFAController.send2FALoginCode called for " + sendTo);
        long userId = (long) request.getAttribute(USER_ID);
        String returnCode = "WRONG_ARGUMENT";
        String returnMessage = "Wrong value for parameter 'type'";

        try {
            if ("email".equals(type)) {
                ReturnCodeMessageResponse response = twoFAMapper.verifyEmail(userId, sendTo);
                returnCode = response.getReturnCode();
                returnMessage = response.getReturnMessage();
                if ("SUCCESS".equals(returnCode)) {
                    emailServiceFacade.sendVerificationCode(
                            sendTo, TwoFAManager.getInstance().generateCode(getToken(request)));
                }
            }
        } catch (Exception e) {
            returnCode = "SEND_VERIFICATION_CODE_ERROR";
            returnMessage = e.getMessage();
        }

        return new ReturnCodeMessageResponse(returnCode, returnMessage);
    }

    @PutMapping(value = "/2fa_login_code", produces = { "application/JSON" })
    public LoginResponse verify2FALoginCode(
            @RequestParam(value = "code") final String code,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        logger.info("TwoFAController.verify2FALoginCode called for userId " + userId);
        ResponseCode response;
        String new_token = "";
        int attemptsAvailable = 0;

        Verify2FACodeResult result = TwoFAManager.getInstance().verifyCode(getToken(request), code);
        if (result.getValid()) {
            RequestData requestData = new RequestData(request);
            new_token = SessionManager.getInstance().generateSessionToken(userId, requestData.getTokenPrefix());
            response = ResponseCode.SUCCESS;
        } else if (result.getAttempts() < 3) {
            attemptsAvailable = 3 - result.getAttempts();
            response = ResponseCode.CODE_INVALID;
        } else {
            response = ResponseCode.TOO_MANY_ATTEMPTS;
        }

        return new LoginResponse(new_token, null, attemptsAvailable, response.name(), response.getMessage());
    }
}
