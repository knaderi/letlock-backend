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
        } else {
            if (settingValueStr.contentEquals("true")){
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
    public BooleanResponse refreshAppSettings() {

        logger.info("MgmtController.refreshAppSettings called");
        String returnCode = "SUCCESS";
        String returnMessage = "";
        boolean result = false;
        try {
            appSettingManager.loadAppsSettings();
            result = true;
        } catch (Exception e) {
            returnCode = "REFRESH_SETTINGS_ERROR";
            logger.error("MgmtController.refreshAppSettings failed."
                    + " returnCode: "
                    + returnCode
                    + " returnMessage: "
                    + e.getMessage());
        }
        return new BooleanResponse(result, returnCode, returnMessage);

    }
    
    //TODO: Not integrated or working - check the unit tests and debug the integration
    @RequestMapping(method = RequestMethod.POST, value = "/setting/add_redeem_code", produces = { "application/JSON" })
    public BooleanResponse addRedeemCode(
            @RequestParam(value = "packageId") final int packageId,
            @RequestParam(value = "redeemCode") final String redeemCode,
            @RequestParam(value = "partnerName") final String partnerName,
            @RequestParam(value = "action") final String action,
            @RequestParam(value = "validUntil") final Timestamp validUntil,
            @RequestParam(value = "discountValue") final BigDecimal discountValue,
            @RequestParam(value = "discountUnit") final String discountUnit) {

        logger.info("MgmtController.addRedeemCode called");
        if (!action.contentEquals("SIGNUP") && !action.contentEquals("CHECKOUT")) {
            return new BooleanResponse(false, "INVALID_ACTION", "Valid actions are signup or checkout");
        }
        String returnCode = "SUCCESS";
        String returnMessage = "";
        boolean result = false;
        try {
            mgmtMapper.addRedeemCode(packageId, redeemCode, partnerName, action, validUntil, discountValue, discountUnit);
            result = true;
        } catch (Exception e) {
            returnCode = "ADD_REDEEM_CODE_ERROR";
            logger.error("MgmtController.addRedeemCode failed."
                    + " returnCode: "
                    + returnCode
                    + " returnMessage: "
                    + e.getMessage());
        }
        return new BooleanResponse(result, returnCode, returnMessage);

    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/setting/update_apps_setting", produces = { "application/JSON" })
    public BooleanResponse updateAppsSetting(
            @RequestParam(value = "key") final String key,
            @RequestParam(value = "value") final String value,
            @RequestParam(value = "app") final String app) {
        logger.info("MgmtController.updateAppsSetting called");
        String returnCode = "SUCCESS";
        String returnMessage = "";
        boolean result = false;
        try {
            mgmtMapper.updateAppSettings(key, value, app);
            appSettingManager.loadAppsSettings(); //load the new settings.
            result = true;
        } catch (Exception e) {
            returnCode = "UPDATE_SETTINGS_ERROR";
            logger.error("MgmtController.updateAppsSetting failed."
                    + " returnCode: "
                    + returnCode
                    + " returnMessage: "
                    + e.getMessage());
        }
        return new BooleanResponse(result, returnCode, returnMessage);

        
    }

}
