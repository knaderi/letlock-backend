package com.landedexperts.letlock.filetransfer.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface RemoteStorageService {

    ResponseEntity<Resource> downloadRemoteFile(final String remotePathName);

    void uploadFileToRemote(final String localFilePath, final String remoteFilePath);
}
