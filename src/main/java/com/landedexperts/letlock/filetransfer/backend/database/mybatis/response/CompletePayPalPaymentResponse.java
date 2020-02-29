/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
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
