/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.service;

import java.io.File;
import java.io.FileInputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.BackendTestConstants;
import com.landedexperts.letlock.filetransfer.backend.controller.FileController;

public class S3StorageServiceTest extends AbstractTest implements BackendTestConstants {

    @Value("${s3.storage.bucket}")
    private String s3Bucket;

    @Autowired
    private S3StorageService S3StorageService;

    @Autowired
    private FileController fileControllerService;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testUploadFile() throws Exception {
        File newFile = new File("C:\\Users\\rkaur\\LetLock\\files\\100MB");
        // File newFile = new File("c:\\LetLock-FileTransfer-Setup.exe");
        boolean flag = newFile.exists();
        FileInputStream inputStream = new FileInputStream(newFile);
        try {
            Thread t1 = new Thread(new S3StorageServiceTest().new CheckProgress());
            t1.start();
            MultipartFile localFile = new MockMultipartFile(newFile.getName(), newFile.getName(), null, inputStream);
            S3StorageService.uploadFileToRemote(localFile, localFile.getName(), 1.08837101E8);
        } catch (Error e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
    }

    private class CheckProgress implements Runnable {

        @Override
        public void run() {
            double pct = 0.00;
            while (true) {
                pct = new S3StorageService().getUploadSize("100MB");
     //           if (pct > 0) {
                    System.out.println("Upload size is " + pct);
       //         }
            }

        }

    }

    @Test
    public void testDownloadFile() throws Exception {

//        ResponseEntity<Resource> downloadInputStream = S3StorageService.downloadRemoteFile(TEST_FILE_NAME);
//        ByteArrayOutputStream result = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int length;
//        InputStream inputStream = downloadInputStream.getBody().getInputStream();
//        while ((length = inputStream.read(buffer)) != -1) {
//            result.write(buffer, 0, length);
//        }
//        Assertions.assertEquals(TEST_FILE_CONTENT, result.toString(StandardCharsets.UTF_8.name()),
//                "testDownloadFile: Should get test file content");
    }

}
