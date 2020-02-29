/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface RemoteStorageService {

    ResponseEntity<Resource> downloadRemoteFile(final String remotePathName);

    void uploadFileToRemote(final String localFilePath, final String remoteFilePath);
}
