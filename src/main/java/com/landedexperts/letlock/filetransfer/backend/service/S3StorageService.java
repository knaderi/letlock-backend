package com.landedexperts.letlock.filetransfer.backend.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

@Service
public class S3StorageService implements RemoteStorageService {

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
        return ResponseEntity.ok().contentLength(fullObject.getObjectMetadata().getContentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(isr);
    }

    @Override
    public void uploadFileToRemote(String localFilePath, String remoteFilePath) {
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

}
