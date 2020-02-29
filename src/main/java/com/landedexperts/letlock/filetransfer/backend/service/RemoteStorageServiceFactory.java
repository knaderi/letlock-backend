/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.amazonaws.services.pinpointemail.model.NotFoundException;

@Service
public class RemoteStorageServiceFactory implements ApplicationContextAware {
    
    private static ApplicationContext applicationContext;

    
    public RemoteStorageService getRemoteStorageService(String service) {

        if ("S3".equalsIgnoreCase(service)) {
            return getBean(S3StorageService.class);
        } 
        if ("GDRIVE".equalsIgnoreCase(service)) {
            throw new NotFoundException("Google Drive Service is not found." );
        } 
        if ("DropBox".equalsIgnoreCase(service)) {
            throw new NotFoundException("DropBox Service is not found." );
        } 
        return getBean(S3StorageService.class);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

}
