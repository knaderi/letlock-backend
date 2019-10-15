package com.landedexperts.letlock.filetransfer.backend.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.amazonaws.services.pinpointemail.model.NotFoundException;

@Service
public class RemoteStorageServiceFactory {
    
    private ApplicationContext applicationContext;

    public RemoteStorageServiceFactory() {
        applicationContext = new AnnotationConfigApplicationContext(
                S3StorageService.class               
        );
        /* I have added LocationService.class because this component is also autowired */
    }
    
    public RemoteStorageService getRemoteStorageService(String service) {

        if ("S3".equalsIgnoreCase(service)) {
            return applicationContext.getBean(S3StorageService.class);
        } 
        if ("GDRIVE".equalsIgnoreCase(service)) {
            throw new NotFoundException("Google Drive Service is not found." );
        } 
        if ("DropBox".equalsIgnoreCase(service)) {
            throw new NotFoundException("DropBox Service is not found." );
        } 
        return applicationContext.getBean(S3StorageService.class);
    }

}
