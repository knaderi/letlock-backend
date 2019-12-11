package com.landedexperts.letlock.filetransfer.backend.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
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
    @Before
    public void setUp() throws Exception{
        super.setUp();
    }

    private UUID testUUId = getUuid();

    @Test
    public void testUploadFileWithNoTransferSession() throws Exception {
        SessionTokenResponse response = userController.login(TEST_USER_ID, TEST_PASSWORD);
        MultipartFile localFile = new MockMultipartFile(TEST_FILE_NAME, TEST_FILE_CONTENT.getBytes());
        String token = response.getToken();
        BooleanResponse uploadResponse = fileController.uploadFile(token, testUUId, localFile);
        assertFalse("upload should fail as there is no transfer session", uploadResponse.getResult());
    }

    @Test
    public void testDownloadFileFailingForNoTransferSession() throws Exception {
        SessionTokenResponse response = userController.login(TEST_USER_ID, TEST_PASSWORD);
        String token = response.getToken();
        ResponseEntity<Resource> fileDownloadResponse = fileController.downloadFile(token, testUUId);
        // download should fail.
        assertEquals("download should fail", FileController.DOWNLOAD_FAILED, fileDownloadResponse.getBody().toString());
    }

    private UUID getUuid() {
        return UUID.randomUUID();
    }


}
