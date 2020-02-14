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
