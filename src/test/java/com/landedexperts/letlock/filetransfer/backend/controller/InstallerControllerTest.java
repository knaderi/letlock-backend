/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;

public class InstallerControllerTest extends BaseControllerTest {

    @Autowired
    private InstallerController adminController;


    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }


    @Test
    public void testGetWinInstallersList() throws Exception {
        createLoggedInActiveUser();
        login();
        JsonResponse<List<S3ObjectSummary>> installersResponse = adminController.getWindowsInstallers(token);
        Assertions.assertNotNull(installersResponse.getResult(), "There should be alist of installers.");
    }

    @Test
    public void testGetMacInstallersList() throws Exception {
        createLoggedInActiveUser();
        login();
        JsonResponse<List<S3ObjectSummary>> installersResponse = adminController.getMacInstallers(token);
        Assertions.assertNotNull(installersResponse.getResult(), "There should be alist of installers.");
    }
    
    @Test
    public void testDownloadWindowsInstaller() throws Exception {
        createLoggedInActiveUser();
        login();
        ResponseEntity<Resource> fileDownloadResponse = adminController.downloadWindowsInstaller(token);
        Assertions.assertNotNull(fileDownloadResponse);
    }
}
