package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import java.util.Date;

public class FileUploadResponse extends ReturnCodeMessageResponse {
    private String remotePathName;
    private Date expires;
    
    public FileUploadResponse() {
        super();
    }
    
    public FileUploadResponse(final String remotePathName, final Date expires, final String returnCode, final String returnMessage) {
        super(returnCode, returnMessage);
        this.remotePathName = remotePathName;
        this.expires = expires;
    }
    
    public String getRemotePathName() {
        return this.remotePathName;
    }

    public Date getExpires() {
        return this.expires;
    }
}

