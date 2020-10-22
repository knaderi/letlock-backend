/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.FileMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;
import com.landedexperts.letlock.filetransfer.backend.service.RemoteStorageServiceFactory;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class InstallerController {

    private static final String WIN_LET_LOCK_FILE_TRANSFER_SETUP = "win/LetLock-FileTransfer";
    private static final String MAC_LET_LOCK_FILE_TRANSFER_SETUP = "mac/LetLock-FileTransfer";

    private static final String DEFAULT_REMOTE_STORAGE = "S3";

    private final Logger logger = LoggerFactory.getLogger(InstallerController.class);


    @Autowired
    FileMapper fileMapper;

    @Autowired
    RemoteStorageServiceFactory remoteStorageService;

    @RequestMapping(method = RequestMethod.GET, value = "/installer/windows/list", produces = { "application/JSON" })
    public JsonResponse<List<S3ObjectSummary>> getWindowsInstallers(@RequestParam(value = "token") final String token) {

        return getInstallersForPlatform(token, WIN_LET_LOCK_FILE_TRANSFER_SETUP);

    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/installer/mac/list", produces = { "application/JSON" })
    public JsonResponse<List<S3ObjectSummary>> getMacInstallers(@RequestParam(value = "token") final String token) {

        return getInstallersForPlatform(token, MAC_LET_LOCK_FILE_TRANSFER_SETUP);

    }

    private JsonResponse<List<S3ObjectSummary>> getInstallersForPlatform(final String token, final String platformInstallerPath) {
        logger.info("FileController.upload_file called for token " + token);
        List<S3ObjectSummary> s3ObjectsSummaries = new ArrayList<S3ObjectSummary>();
        String returnCode = "SUCCESS";
        String returnMessage = "";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            s3ObjectsSummaries = remoteStorageService.getRemoteStorageService(DEFAULT_REMOTE_STORAGE).getInstallersList(platformInstallerPath);
        } else {
            returnCode = "TOKEN_INVALID";
            returnMessage = "Invalid token";
        }

        logger.info("FileController.uploadFile returning response with result " + s3ObjectsSummaries);
        Predicate<S3ObjectSummary> byPath = S3ObjectSummary -> S3ObjectSummary.getKey().startsWith(platformInstallerPath);
        List<S3ObjectSummary> filteredSummaries = s3ObjectsSummaries.parallelStream().filter(byPath).sorted(Comparator.comparing(S3ObjectSummary::getKey)).collect(Collectors.toList());

        return new JsonResponse<List<S3ObjectSummary>>(filteredSummaries, returnCode, returnMessage);
    }
    

    
    @RequestMapping(method = RequestMethod.GET, value = "/installer/windows/download")
    public ResponseEntity<Resource> downloadWindowsInstaller(@RequestParam(value = "token") final String token) throws Exception {
        logger.info("FileController.downloadWindowsInstaller called for token " + token);
        JsonResponse<List<S3ObjectSummary>> winInstallers = getWindowsInstallers(token);
        if(winInstallers.getReturnCode().equals("SUCCESS") && ! winInstallers.getResult().isEmpty()) {
            S3ObjectSummary installerSummary = winInstallers.getResult().get(0);
            return remoteStorageService.getRemoteStorageService(DEFAULT_REMOTE_STORAGE).downloadInstallerFile(installerSummary.getKey());
        } else {
            //TODO: email administrator as the download failed.
            return null;
        }
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/installer/mac/download")
    public ResponseEntity<Resource> downloadMacInstaller(@RequestParam(value = "token") final String token) throws Exception {
        logger.info("FileController.downloadMacInstaller called for token " + token);
        JsonResponse<List<S3ObjectSummary>> macInstallers = getMacInstallers(token);
        if(macInstallers.getReturnCode().equals("SUCCESS") && ! macInstallers.getResult().isEmpty()) {
            S3ObjectSummary installerSummary = macInstallers.getResult().get(0);
            return remoteStorageService.getRemoteStorageService(DEFAULT_REMOTE_STORAGE).downloadInstallerFile(installerSummary.getKey());
        } else {
            //TODO: email administrator as the download failed.
            return null;
        }
    }

}
