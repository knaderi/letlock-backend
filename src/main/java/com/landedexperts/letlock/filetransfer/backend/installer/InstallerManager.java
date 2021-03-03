package com.landedexperts.letlock.filetransfer.backend.installer;

import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.DEFAULT_REMOTE_STORAGE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.landedexperts.letlock.filetransfer.backend.service.RemoteStorageServiceFactory;
import com.landedexperts.letlock.filetransfer.backend.utils.AdminNotification;

@Service
@CacheConfig(cacheNames = "installers")
public class InstallerManager {

    private static final String ACTION = "Get download link for desktop app installer";

    @Autowired
    RemoteStorageServiceFactory remoteStorageService;
    
    @Autowired
    AdminNotification adminNotification;

    @Cacheable (unless = "#result.isEmpty()")
    public String getDownloadLink(final InstallerPlatform platform) throws Exception {
        String downloadLink = "";
        try {
            String latestStr = remoteStorageService.getRemoteStorageService(DEFAULT_REMOTE_STORAGE)
                    .getInstallersInfo(platform.getConfig());
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            InstallersInfo latest = mapper.readValue(latestStr, InstallersInfo.class);
            
            downloadLink = remoteStorageService.getRemoteStorageService(DEFAULT_REMOTE_STORAGE)
                    .getInstallerUrl(latest.getInstallerName());
        } catch(Exception e) {
            adminNotification.actionFailure(ACTION, e);
            throw new Exception(e.getMessage());
        }
        return downloadLink;
    }
    
    @CacheEvict(allEntries = true)
    public void clearInstallersCache() {}

    
}
