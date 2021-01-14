package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;


public class LoginResponse extends ReturnCodeMessageResponse {
    private ResultObject result = new ResultObject();

    public LoginResponse(String token, Boolean twoFARequired, String returnCode, String returnMessage) {
        super(returnCode, returnMessage);
        setToken(token);
        setTwoFARequired(twoFARequired);
    }

    public ResultObject getResult() {
        return result;
    }

    public void setToken(String token) {
        result.setToken(token);
    }
    
    public void setTwoFARequired(Boolean twoFARequired) {
        result.setTwoFARequired(twoFARequired);
    }

    public class ResultObject {
        private String token;
        private Boolean twoFARequired;

        public ResultObject() {
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
        
        public Boolean getTwoFARequired() {
            return twoFARequired;
        }

        public void setTwoFARequired(Boolean twoFARequired) {
            this.twoFARequired = twoFARequired;
        }
    }
}
