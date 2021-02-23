package com.landedexperts.letlock.filetransfer.backend.installer;

public class InstallerInfoFile {
    private String url = "";
    private String sha512 = "";
    private int size = 0;
    private int blockMapSize = 0;
    
    public InstallerInfoFile() {
    }
    
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getSha512() {
        return sha512;
    }
    public void setSha512(String sha512) {
        this.sha512 = sha512;
    }
    
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    
    public int getBlockMapSize() {
        return blockMapSize;
    }
    public void setBlockMapSize(int blockMapSize) {
        this.blockMapSize = blockMapSize;
    }
    
}

