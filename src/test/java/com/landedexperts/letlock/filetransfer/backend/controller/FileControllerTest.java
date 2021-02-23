/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.USER_ID;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.SessionTokenResponse;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

public class FileControllerTest extends BaseControllerTest {

    @Autowired
    private FileController fileController;

    @Autowired
    private UserController userController;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    private UUID testUUId = getUuid();
    
    @Test
    public void testUploadFileWithNoTransferSession() throws Exception {
        MultipartFile localFile = new MockMultipartFile(TEST_FILE_NAME, TEST_FILE_CONTENT.getBytes());
        MockHttpServletRequest request = getRequest();
        BooleanResponse uploadResponse = fileController.uploadFile(testUUId, localFile, request);
        Assertions.assertFalse(uploadResponse.getResult().getValue(), "upload should fail as there is no transfer session");
    }

    @Test
    public void testDownloadFileFailingForNoTransferSession() throws Exception {
        MockHttpServletRequest request = getRequest();
        ResponseEntity<Resource> fileDownloadResponse = fileController.downloadFile(testUUId, request);
        // download should fail.
        Assertions.assertEquals(FileController.DOWNLOAD_FAILED, fileDownloadResponse.getBody().toString(), "download should fail");
    }

    private UUID getUuid() {
        return UUID.randomUUID();
    }
    
    private MockHttpServletRequest getRequest() throws Exception{
        SessionTokenResponse response = userController.login(TEST_USER_ID, TEST_PASSWORD, new MockHttpServletRequest());
        String token = response.getResult().getToken();
        long userId = SessionManager.getInstance().getUserId(token);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("Authorization", "Bearer " + token);
        request.setAttribute(USER_ID, userId);

        return request;
    }

}
