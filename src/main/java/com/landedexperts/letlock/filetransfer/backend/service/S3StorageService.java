/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.service;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
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
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.regions.Regions;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.Transfer.TransferState;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

@Service
public class S3StorageService implements RemoteStorageService {

    private static final int UPLOAD_THREADPOOL_SIZE = 10;

    private static final int UPLOAD_MAX_RETRY = 5;

    private static final int MINIMUM_UPLOAD_PART_SIZE = 5 * 1024 * 1024; // 5MB

    private static final int UPLOAD_TRESHHOLD = 1000 * 1024 * 1024; // 1GB

    private final Logger logger = LoggerFactory.getLogger(S3StorageService.class);
    private static ConcurrentHashMap<String, Double> uploadProgressMap = new ConcurrentHashMap<String, Double>();

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
    public void uploadFileToRemote(MultipartFile multipartFile, String remoteFilePath, double fileByteSize) {
        Regions clientRegion = Regions.DEFAULT_REGION;

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();

            StopWatch watch = new StopWatch("TM stop watch is being set");
            watch.start();
            uploadUsingTransferManager(remoteFilePath, s3Client, multipartFile, fileByteSize);
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
        } finally {
            removeProgressRecord(multipartFile.getName());
        }

    }

    private void uploadUsingTransferManager(String remoteFilePath, AmazonS3 s3Client, MultipartFile multipartFile, Double fileByteSize) throws Exception {
        // final File file = convertMultiPartFileToFile(multipartFile);
        ClientConfiguration clientConfiguration = new ClientConfiguration();
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
        updateProgressReport(multipartFile.getName(), 0.0);
        upload.addProgressListener(new ProgressListener() {
            public void progressChanged(ProgressEvent e) {
//                double pct = (e.getBytesTransferred() + getUploadProgress)* 100.0 / fileByteSize;
                updateProgressReport(multipartFile.getName(), e.getBytesTransferred());
            }
        });
        // Optionally, wait for the upload to finish before continuing.
        upload.waitForCompletion();
        // print the final state of the transfer.
        TransferState xfer_state = upload.getState();
        System.out.println(": " + xfer_state);
    }

    // prints a simple text progressbar: [##### ]
    public static void updateProgressReport(String name, double uploadedChunkSize) {
        Double existinuploadSize = uploadProgressMap.get(name);
        if(existinuploadSize == null) {
            existinuploadSize = 0.00;
        }
        System.out.println("adding " + existinuploadSize + " with " + uploadedChunkSize);
       double updatedUploadSize = existinuploadSize + uploadedChunkSize;
        uploadProgressMap.put(name, updatedUploadSize);
//        // if bar_size changes, then change erase_bar (in eraseProgressBar) to
//        // match.
//        final int bar_size = 40;
//        final String empty_bar = "                                        ";
//        final String filled_bar = "########################################";
//        int amt_full = (int) (bar_size * (pct / 100.0));
//        System.out.format("  [%s%s]", filled_bar.substring(0, amt_full),
//                empty_bar.substring(0, bar_size - amt_full));
    }

    @Override
    public double getUploadSize(String name) {
        if(uploadProgressMap.containsKey(name)) {
            return uploadProgressMap.get(name);
        }else {
            return 0;
        }
    }

    // erases the progress bar.
    public static void removeProgressRecord(String name) {
        uploadProgressMap.remove(name);
//        // erase_bar is bar_size (from printProgressBar) + 4 chars.
//        final String erase_bar = "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b";
//        System.out.format(erase_bar);
    }


}
