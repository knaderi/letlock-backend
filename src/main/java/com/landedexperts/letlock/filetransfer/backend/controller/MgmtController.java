/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.MgmtMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.session.AppSettingsManager;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class MgmtController {

    private final Logger logger = LoggerFactory.getLogger(MgmtController.class);
    @Autowired
    AppSettingsManager appSettingManager;

    @Autowired
    MgmtMapper mgmtMapper;

    @GetMapping(value = "/setting/is_free_signup_credit_for_app", produces = { "application/JSON" })
    public JsonResponse<Boolean> isFreeSignUpTransferCreditForApp(
            @RequestParam(value = "appName") final String appName) {
        logger.info("MgmtController.isFreeSignUpTransferCredit called for app " + appName);
        String settingValueStr = appSettingManager.getSettingValue(appName, "signup_free_credit");
        boolean settingValue = false;
        String returnCode = "SUCCESS";
        String returnMessage = "";

        if ("NOT_FOUND".equals(settingValueStr)) {
            return new JsonResponse<Boolean>(false, "APP_SETTING_NOT_FOUND",
                    "Did not find any active setting for the specified app.");
        }else {
            if(settingValueStr.contentEquals("true")){
                settingValue = true;
            }
            
        }
        return new JsonResponse<Boolean>(settingValue, returnCode, returnMessage);

    }

    @GetMapping(value = "/setting/is_free_signup_credit", produces = { "application/JSON" })
    public JsonResponse<Boolean> isFreeSignUpTransferCredit() {
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
    
    //TODO: Not integreated or working - check the unit tests and debug the integration
    @RequestMapping(method = RequestMethod.POST, value = "/setting/add_redeem_code", produces = { "application/JSON" })
    public BooleanResponse addRedeemCode(
            @RequestParam(value = "token") final String token,
            @RequestParam(value = "packageId") final int packageId,
            @RequestParam(value = "redeemCode") final String redeemCode,
            @RequestParam(value = "partnerName") final String partnerName,
            @RequestParam(value = "action") final String action,
            @RequestParam(value = "validUntil") final Timestamp validUntil,
            @RequestParam(value = "discountValue") final BigDecimal discountValue,
            @RequestParam(value = "discountUnit") final String discountUnit) {

        logger.info("UserController.refreshAppSettings called");
        String returnCode = "SUCCESS";
        String returnMessage = "";
        long userId = SessionManager.getInstance().getUserId(token);
        boolean result = true;
        try {
            if(!action.contentEquals("SIGNUP") && !action.contentEquals("CHECKOUT")) {
                returnCode = "INVALID_ACTION";
                returnMessage = "valid actions are signup or checkout";
                result = false;
            } if (userId > 0 && userId == 1) {// TODO: check for admin role later
                mgmtMapper.addRedeemCode(packageId, redeemCode, partnerName, action, validUntil, discountValue, discountUnit);

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
    
    @RequestMapping(method = RequestMethod.POST, value = "/setting/update_apps_setting", produces = { "application/JSON" })
    public BooleanResponse updateAppsSetting(
            @RequestParam(value = "token") final String token,
            @RequestParam(value = "key") final String key,
            @RequestParam(value = "value") final String value,
            @RequestParam(value = "app") final String app) {
        logger.info("UserController.updateAppsSetting called");
        String returnCode = "SUCCESS";
        String returnMessage = "";
        long userId = SessionManager.getInstance().getUserId(token);
        boolean result = true;
        try {
           if (userId > 0 && userId == 1) {// TODO: check for admin role later
                mgmtMapper.updateAppSettings(key, value, app);

            } else {
                returnCode = "UPDATE_SETTINGS_FAIED";
                returnMessage = "Updating app settings failed";
                result = false;
            }
        } catch (Exception e) {
            returnCode = "UPDATE_SETTINGS_ERROR";
            logger.error("UserController.updateAppsSetting failed."
                    + " returnCode: "
                    + returnCode
                    + " returnMessage: "
                    + e.getMessage());
        }
        return new BooleanResponse(result, returnCode, returnMessage);

        
    }

}
