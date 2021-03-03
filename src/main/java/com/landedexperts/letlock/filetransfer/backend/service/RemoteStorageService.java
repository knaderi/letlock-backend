/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface RemoteStorageService {

    ResponseEntity<Resource> downloadRemoteFile(final String remotePathName);
    
    void uploadFileToRemote(final MultipartFile file, final String remoteStoragePath );

    String getInstallersInfo(String remotePathName);
    
    String getInstallerUrl(String remotePathName);
    
}
