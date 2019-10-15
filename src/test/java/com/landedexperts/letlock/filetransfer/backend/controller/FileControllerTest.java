package com.landedexperts.letlock.filetransfer.backend.controller;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.landedexperts.letlock.filetransfer.backend.AbstractTest;

public class FileControllerTest extends AbstractTest{
    
    private static final String TEST_FILE_NAME = "testSaveFile.txt";

    private static final String TEST_FILE_CONTENT = "This is a test file";

    @Value("${s3.storage.bucket}")
    private String s3Bucket;
    
    @Autowired
    private FileController fileController;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    
    @Test
    public void testUploadFile() throws Exception {
        MultipartFile localFile = new MockMultipartFile(TEST_FILE_NAME, TEST_FILE_CONTENT.getBytes());
        String localFilePath = System.getProperty("user.home") + File.separator + TEST_FILE_NAME;
        String remoteFilePath = ".";
        fileController.saveFileOnDisk(localFile, localFilePath);
        fileController.uploadFileToRemote(localFilePath, remoteFilePath);   
    }
    
    @Test
    public void testDownloadFile() throws Exception {
 
        ResponseEntity<Resource>  downloadInputStream= fileController.downloadRemoteFile(TEST_FILE_NAME);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        InputStream inputStream = downloadInputStream.getBody().getInputStream();
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        assertEquals(TEST_FILE_CONTENT, result.toString(StandardCharsets.UTF_8.name()));        
    }

}
