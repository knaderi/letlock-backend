package com.landedexperts.letlock.filetransfer.backend.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DescriptiveResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.FileMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.BooleanPathnameVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.service.RemoteStorageServiceFactory;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

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

    @RequestMapping(method = RequestMethod.POST, value = "/upload_file", produces = { "application/JSON" })
    public BooleanResponse uploadFile(@RequestParam(value = "token") final String token,
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid, @RequestParam(value = "file") final MultipartFile file)
            throws Exception {
        logger.info("FileController.fileInsert called for token " + token);
        boolean result = false;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";
        

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            // Get the path of the uploaded file
            String localFilePath = System.getProperty("user.home") + File.separator + UUID.randomUUID().toString();
            String remotePathName = UUID.randomUUID().toString(); //we use the uuid as the pathname to the file. This is used a the key name on s3.

            // Set the expiry date
            Date expires = new Date((new Date()).getTime() + FileController.fileLifespan);

            IdVO answer = fileMapper.insertFileUploadRecord(userId, fileTransferUuid, remotePathName, expires);

            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();

            result = returnCode.equals("SUCCESS");

            if (result) {
                saveFileOnDisk(file, localFilePath);
                remoteStorageService.getRemoteStorageService(DEFAULT_REMOTE_STORAGE).uploadFileToRemote(localFilePath, remotePathName);
            }
        }
        logger.info("FileController.uploadFile returning response with result " + result);

        return new BooleanResponse(result, returnCode, returnMessage);
    }

    public void saveFileOnDisk(final MultipartFile localFile, String localFilePath) throws IOException, FileNotFoundException {
        InputStream fileStream = localFile.getInputStream();
        OutputStream localFileCopy = new FileOutputStream(localFilePath);
        try {
            IOUtils.copy(fileStream, localFileCopy);
        } finally {
            IOUtils.closeQuietly(fileStream);
            IOUtils.closeQuietly(localFileCopy);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/can_download_file")
    public BooleanResponse canDownloadFile(@RequestParam(value = "token") final String token,
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid) throws Exception {
        logger.info("FileController.canDownloadFile called for token " + token);
        boolean result = false;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            BooleanPathnameVO isAllowed = fileMapper.isAllowedToDownloadFile(userId, fileTransferUuid);

            result = isAllowed.getValue();
            returnCode = isAllowed.getReturnCode();
            returnMessage = isAllowed.getReturnMessage();
        }
        logger.info("FileController.canDownloadFile returning response " + result);
        return new BooleanResponse(result, returnCode, returnMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/download_file")
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "token") final String token,
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid) throws Exception {
        logger.info("FileController.downloadFile called for token " + token);
        @SuppressWarnings("unused")
        String returnCode = "TOKEN_INVALID";
        @SuppressWarnings("unused")
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            BooleanPathnameVO isAllowed = fileMapper.isAllowedToDownloadFile(userId, fileTransferUuid);
            if (isAllowed.getValue()) {
                return remoteStorageService.getRemoteStorageService(DEFAULT_REMOTE_STORAGE).downloadRemoteFile(isAllowed.getPathName());
            }
        }
        logger.error("Cannot download file(s). No authenticated user was found for given token: " + token);
        return ResponseEntity.badRequest().contentLength(0).contentType(MediaType.TEXT_PLAIN)
                .body(new DescriptiveResource(DOWNLOAD_FAILED));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete_file", produces = { "application/JSON" })
    public BooleanResponse deleteFile(@RequestParam(value = "token") final String token,
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid) throws Exception {
        logger.info("FileController.deleteFile called for token " + token);
        boolean result = false;
        String returnCode = "TOKEN_INVALID";
        String returnMessage = "Invalid token";

        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
        	ReturnCodeMessageResponse answer = fileMapper.deleteFile(userId, fileTransferUuid);

            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();

            result = returnCode.equals("SUCCESS");

        }
        logger.info("FileController.deleteFile returning response " + result);
        return new BooleanResponse(result, returnCode, returnMessage);
    }

}
