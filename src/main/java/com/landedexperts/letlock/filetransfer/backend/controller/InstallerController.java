/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Irina Soboleva - 2021
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.installer.InstallerManager;
import com.landedexperts.letlock.filetransfer.backend.installer.InstallerPlatform;
import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.INSTALLERS_CACHE_RESET_INTERVAL;;

@RestController
@RequestMapping(value = "/installer", produces = { "application/JSON" })
public class InstallerController {
    
    @Autowired
    InstallerManager installerManager;

    @GetMapping(value = "/windows")
    public JsonResponse<String> getWindowsInstallers() throws Exception {
        String downloadLink = installerManager.getDownloadLink(InstallerPlatform.WIN);
        return new JsonResponse<String>(downloadLink, "SUCCESS", "");
    }
    
    @GetMapping(value = "/mac")
    public JsonResponse<String> getMacInstallers() throws Exception {
        String downloadLink = installerManager.getDownloadLink(InstallerPlatform.MAC);
        return new JsonResponse<String>(downloadLink, "SUCCESS", "");
    }
    
    @PutMapping
    @Scheduled(fixedDelay = INSTALLERS_CACHE_RESET_INTERVAL, initialDelay = 1000)
    public JsonResponse<Boolean> resetInstallersCache() throws Exception {
        Boolean result = false;
        installerManager.clearInstallersCache();
        try {
            for (InstallerPlatform p : InstallerPlatform.values()) {
                installerManager.getDownloadLink(p);
            }
            result = true;
        } catch (Exception e) {}
        return new JsonResponse<Boolean>(result, "SUCCESS", "");
    }

}
