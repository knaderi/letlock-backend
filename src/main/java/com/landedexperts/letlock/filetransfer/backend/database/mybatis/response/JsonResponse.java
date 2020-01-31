package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import org.springframework.util.StringUtils;

public class JsonResponse extends ReturnCodeMessageResponse {
    private final String result;

    public JsonResponse(final String result, final String returnCode, final String returnMessage) {
        super(returnCode, returnMessage);
        this.result = result;
    }

    public JsonResponse(final String result) {
        super("SUCCESS", "");
        if(StringUtils.isEmpty(result)){
            setReturnCode("NO_LOCATION_FOUND");
            setReturnMessage("No location data was retrived from db.");
        }
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
