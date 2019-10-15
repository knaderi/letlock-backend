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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DescriptiveResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.FileMapper;
import com.landedexperts.letlock.filetransfer.backend.database.vo.BooleanPathnameVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.ErrorCodeMessageVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    /* Timeout in milliseconds, 24 hrs */
    private static long fileLifespan = 86400000L;

    @Autowired
    FileMapper fileMapper;

    @Value("${s3.storage.bucket}")
    private String s3Bucket;

    @RequestMapping(method = RequestMethod.POST, value = "/upload_file", produces = { "application/JSON" })
    public BooleanResponse uploadFile(@RequestParam(value = "token") final String token,
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid, @RequestParam(value = "file") final MultipartFile file)
            throws Exception {
        logger.info("FileController.fileInsert called for token " + token);
        boolean result = false;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";
        String remoteFilePath = ".";

        Integer userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            // Get the path of the uploaded file
            String localFilePath = System.getProperty("user.home") + UUID.randomUUID().toString();

            // Set the expiry date
            Date expires = new Date((new Date()).getTime() + FileController.fileLifespan);

            IdVO answer = fileMapper.insertFileUploadRecord(userId, fileTransferUuid, remoteFilePath, expires);

            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();

            result = errorCode.equals("NO_ERROR");

            saveFileOnDisk(file, localFilePath);
            uploadFileToRemote(localFilePath, remoteFilePath);
        }
        logger.info("FileController.uploadFile returning response with result " + result);

        return new BooleanResponse(result, errorCode, errorMessage);
    }

    void saveFileOnDisk(final MultipartFile localFile, String localFilePath) throws IOException, FileNotFoundException {
        InputStream fileStream = localFile.getInputStream();
        OutputStream localFileCopy = new FileOutputStream(localFilePath);
        try {
            IOUtils.copy(fileStream, localFileCopy);
        } finally {
            IOUtils.closeQuietly(fileStream);
            IOUtils.closeQuietly(localFileCopy);
        }
    }

    void uploadFileToRemote(final String localFilePath, final String remoteFilePath) {
        Regions clientRegion = Regions.DEFAULT_REGION;        
        File localFile = new File(localFilePath);
        String fileObjKeyName = localFile.getName();

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();

            // Upload a file as a new object with ContentType and title specified.
            PutObjectRequest request = new PutObjectRequest(s3Bucket, fileObjKeyName, localFile);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            request.setMetadata(metadata);
            s3Client.putObject(request);
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            logger.error(e.getErrorMessage());
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            logger.error(e.getMessage());
        } finally {
            // TODO: file needs to be removed after moving to use the remote.
            // localFile.delete();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/can_download_file")
    public BooleanResponse canDownloadFile(@RequestParam(value = "token") final String token,
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid) throws Exception {
        logger.info("FileController.canDownloadFile called for token " + token);
        boolean result = false;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        Integer userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            BooleanPathnameVO isAllowed = fileMapper.isAllowedToDownloadFile(userId, fileTransferUuid);

            result = isAllowed.getValue();
            errorCode = isAllowed.getErrorCode();
            errorMessage = isAllowed.getErrorMessage();
        }
        logger.info("FileController.canDownloadFile returning response " + result);
        return new BooleanResponse(result, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/download_file")
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "token") final String token,
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid) throws Exception {
        logger.info("FileController.downloadFile called for token " + token);
        @SuppressWarnings("unused")
        String errorCode = "TOKEN_INVALID";
        @SuppressWarnings("unused")
        String errorMessage = "Invalid token";

        Integer userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            BooleanPathnameVO isAllowed = fileMapper.isAllowedToDownloadFile(userId, fileTransferUuid);
            if (isAllowed.getValue()) {
                    return downloadRemoteFile(isAllowed.getPathName());
//                File file = new File(isAllowed.getPathName());
//                FileInputStream fis = new FileInputStream(file);
//                InputStreamResource isr = new InputStreamResource(fis);
//                return ResponseEntity.ok().contentLength(file.length()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(isr);
            }
        }
        logger.error("Cannot download file(s). No authenticated user was found for given token: " + token);
        return ResponseEntity.badRequest().contentLength(0).contentType(MediaType.TEXT_PLAIN)
                .body(new DescriptiveResource("Download failed."));

        /* Improve answer by responding with the error code and message */
//        String localFilePath = System.getProperty("user.home") + "\\Documents\\projects\\letlock\\letlock-backend\\src\\test\\java\\local_files\\empty";
//		File empty = new File(localFilePath);
//        FileInputStream fisEmpty = new FileInputStream(empty);
//        InputStreamResource isrEmpty = new InputStreamResource(fisEmpty);
//        logger.info("FileController.downloadFile returning responseEntity for entity with length"
//                + isrEmpty.contentLength());
//        return ResponseEntity.ok().contentLength(empty.length()).contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(isrEmpty);
    }

    ResponseEntity<Resource> downloadRemoteFile(String remotePathName) {
        Regions clientRegion = Regions.DEFAULT_REGION;  
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();
        S3Object fullObject = null, objectPortion = null;
        fullObject = s3Client.getObject(new GetObjectRequest(s3Bucket, remotePathName));
        GetObjectRequest rangeObjectRequest = new GetObjectRequest(s3Bucket, remotePathName)
                .withRange(0, fullObject.getObjectMetadata().getContentLength());
        objectPortion = s3Client.getObject(rangeObjectRequest);
        S3ObjectInputStream objectContent = objectPortion.getObjectContent();
        InputStreamResource isr = new InputStreamResource(objectContent);
        return ResponseEntity.ok().contentLength(fullObject.getObjectMetadata().getContentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(isr);
    }

   
    @RequestMapping(method = RequestMethod.POST, value = "/delete_file", produces = { "application/JSON" })
    public BooleanResponse deleteFile(@RequestParam(value = "token") final String token,
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid) throws Exception {
        logger.info("FileController.deleteFile called for token " + token);
        boolean result = false;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        Integer userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            ErrorCodeMessageVO answer = fileMapper.deleteFile(userId, fileTransferUuid);

            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();

            result = errorCode.equals("NO_ERROR");
        }
        logger.info("FileController.deleteFile returning response " + result);
        return new BooleanResponse(result, errorCode, errorMessage);
    }
}
