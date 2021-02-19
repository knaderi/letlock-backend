/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DescriptiveResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.FileMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.BooleanPathnameVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.service.FileUploadException;
import com.landedexperts.letlock.filetransfer.backend.service.RemoteStorageServiceFactory;
import static com.landedexperts.letlock.filetransfer.backend.utils.BackendConstants.USER_ID;

@RestController
public class FileController {

    static final String DOWNLOAD_FAILED = "Download failed.";

    private static final String DEFAULT_REMOTE_STORAGE = "S3";

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    /* Timeout in milliseconds, 24 hrs */
    private static long fileLifespan = 86400000L;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    RemoteStorageServiceFactory remoteStorageService;

    @PostMapping(value = "/upload_file", produces = { "application/JSON" })
    public BooleanResponse uploadFile(
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid,
            @RequestParam(value = "file") final MultipartFile file,
            HttpServletRequest request) throws Exception {
        String returnCode = "SUCCESS";
        String returnMessage = "";

        long userId = (long) request.getAttribute(USER_ID);
        logger.info("FileController.upload_file called for userId " + userId);

        // Get the path of the uploaded file
        
        //String localFilePath = System.getProperty("user.home") + File.separator + UUID.randomUUID().toString();
        //if(StringUtils.isBlank(localFilePath)) {
        //    throw new Exception("local file path");
        //}
        String remotePathName = UUID.randomUUID().toString(); // We use the UUID as the pathname to the file.
                                                              // This is used as the key name on S3.
        // Set the expire date
        Date expires = new Date((new Date()).getTime() + FileController.fileLifespan);

        try {
           // saveFileOnDisk(file, localFilePath);
            remoteStorageService.getRemoteStorageService(DEFAULT_REMOTE_STORAGE).uploadFileToRemote(file, remotePathName);
        } catch (FileUploadException e) {
            e.printStackTrace();
            logger.error("FileController.upload_file could not upload the file due to: " + e.getMessage());
            returnCode = "FILE_UPLOAD_ERROR";
            returnMessage = "Could not upload the file due to: " + e.getMessage();
        }
        if (returnCode.equals("SUCCESS")) {
            IdVO answer = fileMapper.insertFileUploadRecord(userId, fileTransferUuid, remotePathName, expires);
            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();
        }

        logger.info("FileController.uploadFile returning response with result " + returnCode.equals("SUCCESS"));

        return new BooleanResponse(returnCode.equals("SUCCESS"), returnCode, returnMessage);
    }

    public void saveFileOnDisk(final MultipartFile localFile, String localFilePath) throws IOException, FileNotFoundException {
        logger.info("Saving file on disk before upload. localFilePath", localFilePath);
        InputStream fileStream = localFile.getInputStream();
        OutputStream localFileCopy = new FileOutputStream(localFilePath);
        try {
            IOUtils.copy(fileStream, localFileCopy);
        } finally {
            IOUtils.closeQuietly(fileStream);
            IOUtils.closeQuietly(localFileCopy);
        }
    }

    @GetMapping(value = "/can_download_file", produces = { "application/JSON" })
    public BooleanResponse canDownloadFile(
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid,
            HttpServletRequest request) throws Exception {
        logger.info("FileController.canDownloadFile called for file transfer " + fileTransferUuid);

        long userId = (long) request.getAttribute(USER_ID);
        BooleanPathnameVO isAllowed = fileMapper.isAllowedToDownloadFile(userId, fileTransferUuid);

        boolean result = isAllowed.getValue();
        String returnCode = isAllowed.getReturnCode();
        String returnMessage = isAllowed.getReturnMessage();

        logger.info("FileController.canDownloadFile returning response " + result);
        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @PostMapping(value = "/download_file")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid,
            HttpServletRequest request) throws Exception {

        long userId = (long) request.getAttribute(USER_ID);
        logger.info("FileController.downloadFile called for userId " + userId);
        
        BooleanPathnameVO isAllowed = fileMapper.isAllowedToDownloadFile(userId, fileTransferUuid);
        if (isAllowed.getValue()) {
            return remoteStorageService.getRemoteStorageService(DEFAULT_REMOTE_STORAGE).downloadRemoteFile(isAllowed.getPathName());
        }
        return ResponseEntity.badRequest().contentLength(0).contentType(MediaType.TEXT_PLAIN)
                .body(new DescriptiveResource(DOWNLOAD_FAILED));
    }

    @PostMapping(value = "/delete_file", produces = { "application/JSON" })
    public BooleanResponse deleteFile(
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid,
            HttpServletRequest request) throws Exception {

        long userId = (long) request.getAttribute(USER_ID);
        logger.info("FileController.deleteFile called for userId " + userId);

        ReturnCodeMessageResponse answer = fileMapper.deleteFile(userId, fileTransferUuid);

        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();
        boolean result = returnCode.equals("SUCCESS");

        logger.info("FileController.deleteFile returning response " + result);
        return new BooleanResponse(result, returnCode, returnMessage);
    }

}
