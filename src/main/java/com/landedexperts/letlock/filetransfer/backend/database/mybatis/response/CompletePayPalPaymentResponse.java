package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import java.util.Map;

public class CompletePayPalPaymentResponse  extends ReturnCodeMessageResponse{
    private ResultObject result = new ResultObject();

    
    public CompletePayPalPaymentResponse(Map<String, Object> responseMap,  String returnCode, String returnMessage) {
        super(returnCode, returnMessage);
        setResponseMap(responseMap);
    }

    public CompletePayPalPaymentResponse(String returnCode, String returnMessage) {
        super(returnCode, returnMessage);
    }

    public void setResponseMap(Map<String, Object> responseMap) {
        result.setResponseMap(responseMap);
    }

    public ResultObject getResult() {
        return result;
    }


    public class ResultObject {
        private Map<String, Object> responseMap;

        public Map<String, Object> getResponseMap() {
            return responseMap;
        }

        public void setResponseMap(Map<String, Object> responseMap) {
            this.responseMap = responseMap;
        }

        public ResultObject() {
        }

    }
}
