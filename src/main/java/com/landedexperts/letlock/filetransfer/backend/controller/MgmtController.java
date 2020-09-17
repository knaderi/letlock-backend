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

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.session.AppSettingsManager;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class MgmtController {

    private final Logger logger = LoggerFactory.getLogger(MgmtController.class);
    @Autowired
    AppSettingsManager appSettingManager;


    @GetMapping(value = "/setting/is_free_signup_credit_for_app", produces = { "application/JSON" })
    public JsonResponse<String> isFreeSignUpTransferCreditForApp(
            @RequestParam(value = "appName") final String appName) {
        logger.info("MgmtController.isFreeSignUpTransferCredit called for app " + appName);
        String settingValue = appSettingManager.getSettingValue(appName, "signup_free_credit");
        String returnCode = "SUCCESS";
        String returnMessage = "";

        if ("NOT_FOUND".equals(settingValue)) {
            return new JsonResponse<String>(settingValue, "APP_SETTING_NOT_FOUND",
                    "Did not find any active setting for the specified app.");
        }
        return new JsonResponse<String>(settingValue, returnCode, returnMessage);

    }
    
    @GetMapping(value = "/setting/is_free_signup_credit", produces = { "application/JSON" })
    public JsonResponse<String> isFreeSignUpTransferCredit() {
        return isFreeSignUpTransferCreditForApp("all_apps");
    }

    @GetMapping(value = "/setting/refresh_app_settings", produces = { "application/JSON" })
    public BooleanResponse refreshAppSettings(@RequestParam(value = "token") final String token) {

        logger.info("UserController.refreshAppSettings called");
        String returnCode = "SUCCESS";
        String returnMessage = "";
        long userId = SessionManager.getInstance().getUserId(token);
        boolean result = true;
        try {
            if (userId > 0 && userId == 1) {// TODO: check for admin role later
                appSettingManager.loadAppsSettings();
            } else {
                returnCode = "ADMIN_USER_EXPECTED";
                returnMessage = "Admin user is required to add free credit.";
                result = false;
            }
        } catch (Exception e) {
            returnCode = "ADD_TRANSFER_CREDIT_ERROR";
            logger.error("UserController.refreshAppSettings failed."
                    + " returnCode: "
                    + returnCode
                    + " returnMessage: "
                    + e.getMessage());
        }
        return new BooleanResponse(result, returnCode, returnMessage);

    }

}
