package com.landedexperts.letlock.filetransfer.backend.utils;

import java.util.ArrayList;
import java.util.List;

public class LatestInstallersInfo {
    private String version = "";
    private List<InstallerInfoFile> files = new ArrayList<InstallerInfoFile>();
    private String path = "";
    private String sha512 = "";
    private String releaseDate = "";
    
    public LatestInstallersInfo() {
    }
    
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public List<InstallerInfoFile> getFiles() {
        return files;
    }
    public void setFiles(List<InstallerInfoFile> files) {
        this.files = files;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public String getSha512() {
        return sha512;
    }
    public void setSha512(String sha512) {
        this.sha512 = sha512;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    
    public String getInstallerName() {
        files.forEach(file -> {
            if (file.getUrl().contains(".exe") || file.getUrl().contains(".dmg")) {
                path = file.getUrl();
            }
        });
        return path;
    }
}


