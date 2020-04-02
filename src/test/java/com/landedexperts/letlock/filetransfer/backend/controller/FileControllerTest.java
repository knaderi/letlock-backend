/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.BackendTestConstants;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.SessionTokenResponse;

public class FileControllerTest extends AbstractTest implements BackendTestConstants {

    @Autowired
    private FileController fileController;

    @Autowired
    private UserController userController;

    @Override
    @BeforeEach
    public void setUp() throws Exception{
        super.setUp();
    }

    private UUID testUUId = getUuid();

    @Test
    public void testUploadFileWithNoTransferSession() throws Exception {
        SessionTokenResponse response = userController.login(TEST_USER_ID, TEST_PASSWORD);
        MultipartFile localFile = new MockMultipartFile(TEST_FILE_NAME, TEST_FILE_CONTENT.getBytes());
        String token = response.getResult().getToken();
        BooleanResponse uploadResponse = fileController.uploadFile(token, testUUId, localFile);
        Assertions.assertFalse(uploadResponse.getResult().getValue(),"upload should fail as there is no transfer session");
    }

    @Test
    public void testDownloadFileFailingForNoTransferSession() throws Exception {
        SessionTokenResponse response = userController.login(TEST_USER_ID, TEST_PASSWORD);
        String token = response.getResult().getToken();
        ResponseEntity<Resource> fileDownloadResponse = fileController.downloadFile(token, testUUId);
        // download should fail.
        Assertions.assertEquals(FileController.DOWNLOAD_FAILED, fileDownloadResponse.getBody().toString(),"download should fail");
    }

    private UUID getUuid() {
        return UUID.randomUUID();
    }

}
