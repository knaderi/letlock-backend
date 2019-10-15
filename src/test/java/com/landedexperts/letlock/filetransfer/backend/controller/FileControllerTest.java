package com.landedexperts.letlock.filetransfer.backend.controller;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.landedexperts.letlock.filetransfer.backend.AbstractTest;

public class FileControllerTest extends AbstractTest{
    
    @Value("${s3.storage.bucket}")
    private String s3Bucket;
    
    @Autowired
    private FileController fileController;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @BeforeClass
    public static void setSystemProperty() {
        String activeProfile = "dev";
        String mvnCommandLineArgs = System.getenv().get("MAVEN_CMD_LINE_ARGS");
        if (!StringUtils.isEmpty(mvnCommandLineArgs)) {
            int index = mvnCommandLineArgs.indexOf("-Dspring.profiles.active=");
            activeProfile = mvnCommandLineArgs.substring(index + 25);
        }
        System.getProperties().setProperty("spring.profiles.active", activeProfile);

    }
    
    @Test
    public void testSaveFileOnDisk() throws Exception {
        MultipartFile localFile = new MockMultipartFile("testSaveFile.txt", "This is a test file".getBytes());
        String localFilePath = System.getProperty("user.home") + File.separator + "testSaveFile.txt";
        String remoteFilePath = ".";
        fileController.saveFileOnDisk(localFile, localFilePath);
        fileController.uploadFileToRemote(localFilePath, remoteFilePath);   
    }
    
    @Test
    public void testDownloadFile() throws Exception {
 
        ResponseEntity<Resource>  downloadInputStream= fileController.downloadRemoteFile("testSaveFile.txt");
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        InputStream inputStream = downloadInputStream.getBody().getInputStream();
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        assertEquals("This is a test file", result.toString(StandardCharsets.UTF_8.name()));        
    }

}
