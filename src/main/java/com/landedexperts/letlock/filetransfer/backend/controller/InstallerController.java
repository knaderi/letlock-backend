/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Irina Soboleva - 2021
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.service.RemoteStorageServiceFactory;
import com.landedexperts.letlock.filetransfer.backend.utils.LatestInstallersInfo;

@RestController
@RequestMapping(value = "/installer", produces = { "application/JSON" })
public class InstallerController {

    private static final String WIN_LET_LOCK_FILE_TRANSFER_SETUP = "latest.yml";
    private static final String MAC_LET_LOCK_FILE_TRANSFER_SETUP = "latest-mac.yml";

    private static final String DEFAULT_REMOTE_STORAGE = "S3";
    
    private static final String ACTION = "Get download link for desktop app installer";

    @Autowired
    RemoteStorageServiceFactory remoteStorageService;

    @Autowired
    EmailServiceFacade emailServiceFacade;

    @GetMapping(value = "/windows")
    public JsonResponse<String> getWindowsInstallers() throws Exception {
        return getInstallerForPlatform(WIN_LET_LOCK_FILE_TRANSFER_SETUP);
    }
    
    @GetMapping(value = "/mac")
    public JsonResponse<String> getMacInstallers() throws Exception {
        return getInstallerForPlatform(MAC_LET_LOCK_FILE_TRANSFER_SETUP);
    }

    private JsonResponse<String> getInstallerForPlatform(final String platformLatest) throws Exception {
        String downloadLink ="";
        try {
            S3ObjectInputStream latestIS = remoteStorageService.getRemoteStorageService(DEFAULT_REMOTE_STORAGE)
                    .getInstallersInfo(platformLatest);
            String latestStr = IOUtils.toString(latestIS);
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            LatestInstallersInfo latest = mapper.readValue(latestStr, LatestInstallersInfo.class);
            
            downloadLink = remoteStorageService.getRemoteStorageService(DEFAULT_REMOTE_STORAGE)
                    .getInstallerUrl(latest.getInstallerName());
        } catch(Exception e) {
            notifyAdmin(e);
            throw new Exception(e.getMessage());
        }

        return new JsonResponse<String>(downloadLink, "SUCCESS", "");
    }
   
    private void notifyAdmin(Exception e) {
            try {
                emailServiceFacade.sendAdminFailureNotification(ACTION, e.getMessage());
            } catch (Exception err) {
            }
        return;
    }

}
