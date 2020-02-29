/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class SessionTokenResponse extends ReturnCodeMessageResponse {

    private ResultObject result = new ResultObject();

    public SessionTokenResponse(String token, String returnCode, String returnMessage) {
        super(returnCode, returnMessage);
        setToken(token);
    }

    public ResultObject getResult() {
        return result;
    }

    public void setToken(String token) {
        result.setToken(token);
    }

    public class ResultObject {
        private String token;

        public ResultObject() {
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
