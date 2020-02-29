package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public class BooleanResponse extends ReturnCodeMessageResponse {

    public BooleanResponse(boolean resultValue, String returnCode, String returnMessage) {
        super(returnCode, returnMessage);
        setValue(resultValue);
    }

    private ResultObject result = new ResultObject();


    public ResultObject getResult() {
        return result;
    }
    
    public void setValue(boolean value) {
        result.setValue(value);
    }
	
    public class ResultObject{
        private boolean value = false;
        public boolean getValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }

        public ResultObject() {
        }

    }
}
