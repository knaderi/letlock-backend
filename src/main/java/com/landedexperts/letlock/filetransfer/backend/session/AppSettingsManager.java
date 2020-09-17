/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.session;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.MgmtMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.AppsSettingsVO;

@Configuration
public class AppSettingsManager {
    
    @Autowired
    MgmtMapper mgmtMapper;
    
    private final Map<String, AppsSettingsVO> appSettingsMap;

    public AppSettingsManager() {
        appSettingsMap = new HashMap<>();
    }

    
    public void loadAppsSettings() {
        List<AppsSettingsVO> settings = Arrays.asList(mgmtMapper.readSettings());
        settings.forEach(appSettingVO -> {
            appSettingsMap.put(appSettingVO.getApp() + "_" + appSettingVO.getKey(), appSettingVO);
        });
    }
    
    
    public String getSettingValue(String appName, String key) {
        if(appSettingsMap.isEmpty()) {
            loadAppsSettings();
        }
        AppsSettingsVO setting = appSettingsMap.get(appName + "_" + key);
        if(setting == null) {
          return    "NOT_FOUND";
        }
        return setting.getValue();
    }
    
    public boolean isFreeSignUpCreditForClientWeb() {
        return  "true".contentEquals(getSettingValue("client_web_app", "signup_free_credit"));
    }
}
