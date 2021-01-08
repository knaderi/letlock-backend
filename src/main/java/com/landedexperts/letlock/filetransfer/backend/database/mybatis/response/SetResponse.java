package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import java.util.Set;

public class SetResponse<T> extends ReturnCodeMessageResponse {
    private Set<T> result;

    public Set<T> getResult() {
        return result;
    }
    
    public SetResponse(final Set<T> resultValue, final String returnCode, final String returnMessage) {
        super(returnCode, returnMessage);        
        this.result = resultValue;
    }
}
