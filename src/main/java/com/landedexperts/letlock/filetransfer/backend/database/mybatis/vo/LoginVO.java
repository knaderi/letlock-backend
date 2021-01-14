package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public class LoginVO extends ReturnCodeMessageResponse {

    private ResultObject result = new ResultObject();


    public ResultObject getResult() {
        return result;
    }


    public void setId(long id) {
        result.setId(id);
    }
    
    public void setTwoFARequired(boolean twoFARequired) {
        result.setTwoFARequired(twoFARequired);
    }
   
    public class ResultObject{
        private long id = -1;
        private boolean twoFARequired = false;
        public ResultObject() {
        }

        public long getId() {
            return id;
        } 
        
        public void setId(long id) {
            this.id = id;
        }
        
        public boolean getTwoFARequired(){
            return this.twoFARequired;
        }
        
        public void setTwoFARequired(boolean twoFARequired ) {
            this.twoFARequired = twoFARequired;
        }
    }
}
