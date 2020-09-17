/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.MgmtMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class MgmtController {

    private final Logger logger = LoggerFactory.getLogger(MgmtController.class);

    @Autowired
    private MgmtMapper adminMapper;

    @GetMapping(value = "/setting/is_free_signup_credit", produces = { "application/JSON" })
    public JsonResponse<String> isFreeSignUpTransferCredit(
            @RequestParam(value = "appName") final String appName) {
        logger.info("MgmtController.isFreeSignUpTransferCredit called for app " + appName);
        String returnCode = "SUCCESS";
        String returnMessage = "";
        String result = "";
        try {

            JsonResponse<String> answer = adminMapper.isFreeSignupTransferCredit(appName);
            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();
            if ("SUCCESS".equals(returnCode)) {
                result = answer.getResult();
            } else {
                logger.error("MgmtController.isFreeSignUpTransferCredit failed for appName "
                        + appName
                        + " failed. returnCode: "
                        + returnCode
                        + " returnMessage: "
                        + returnMessage);
            }

        } catch (Exception e) {
            returnCode = "APP_TRANSFER_CREDIT_ERROR";
            logger.error("MgmtController.isFreeSignUpTransferCredit failed for appName "
                    + appName
                    + " failed. returnCode: "
                    + returnCode
                    + " returnMessage: "
                    + e.getMessage());
        }
        return new JsonResponse<String>(result, returnCode, returnMessage);
    }

}
