/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.service;

import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

@Service
public class S3StorageService implements RemoteStorageService {

    private static final int UPLOAD_THREADPOOL_SIZE = 10;

    private static final int UPLOAD_MAX_RETRY = 5;

    private static final int MINIMUM_UPLOAD_PART_SIZE = 5*1024*1024; //5MB

    private static final int UPLOAD_TRESHHOLD = 1000*1024*1024; //1GB

    private final Logger logger = LoggerFactory.getLogger(S3StorageService.class);

    @Value("${s3.storage.bucket}")
    private String s3Bucket;

    @Override
    public ResponseEntity<Resource> downloadRemoteFile(String remotePathName) {
        Regions clientRegion = Regions.DEFAULT_REGION;
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();
        S3Object fullObject = null, objectPortion = null;
        fullObject = s3Client.getObject(new GetObjectRequest(s3Bucket, remotePathName));
        GetObjectRequest rangeObjectRequest = new GetObjectRequest(s3Bucket, remotePathName)
                .withRange(0, fullObject.getObjectMetadata().getContentLength());
        objectPortion = s3Client.getObject(rangeObjectRequest);
        S3ObjectInputStream objectContent = objectPortion.getObjectContent();
        InputStreamResource isr = new InputStreamResource(objectContent);
        return ResponseEntity.ok().contentLength(fullObject.getObjectMetadata().getContentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(isr);
    }

    @Override
    public void uploadFileToRemote(MultipartFile multipartFile, String remoteFilePath){
         Regions clientRegion = Regions.DEFAULT_REGION;

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();
            
            StopWatch watch = new StopWatch("TM stop watch is being set");
            watch.start();            
            uploadUsingTransferManager(remoteFilePath, s3Client, multipartFile);
            watch.stop();
            long uploadDuration = watch.getTotalTimeMillis(); 
            logger.debug("Object upload using TM completed in " + uploadDuration + " miliseconds");
            System.out.println("Object upload using TM completed in " + uploadDuration + " miliseconds");

        } catch (AmazonServiceException e) {
            e.printStackTrace();
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            logger.error(e.getErrorMessage());
            throw new FileUploadException(e);
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new FileUploadException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("File upload failed " + e.getMessage());
            throw new FileUploadException(e);
        }

    }

    private void uploadUsingTransferManager(String remoteFilePath, AmazonS3 s3Client, MultipartFile multipartFile) throws Exception {
      //  final File file = convertMultiPartFileToFile(multipartFile);
        ClientConfiguration clientConfiguration=new ClientConfiguration();
        clientConfiguration.setRetryPolicy(
                        PredefinedRetryPolicies.getDefaultRetryPolicyWithCustomMaxRetries(UPLOAD_MAX_RETRY));
        TransferManagerBuilder standard = TransferManagerBuilder.standard();
        standard.setMultipartUploadThreshold(new Long(UPLOAD_TRESHHOLD));
        standard.setMinimumUploadPartSize(new Long(MINIMUM_UPLOAD_PART_SIZE));
        TransferManager tm = standard
                .withS3Client(s3Client).withExecutorFactory(() -> Executors.newFixedThreadPool(UPLOAD_THREADPOOL_SIZE))
                .build();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        Upload upload = tm.upload(s3Bucket, remoteFilePath, multipartFile.getInputStream(), metadata);
        // Optionally, wait for the upload to finish before continuing.
        upload.waitForCompletion();        
    }

}
