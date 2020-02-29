/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public class IdVO extends ReturnCodeMessageResponse {

    private ResultObject result = new ResultObject();


    public ResultObject getResult() {
        return result;
    }


    public void setId(long id) {
        result.setId(id);
    }
   
    public class ResultObject{
        private long id = -1;
        public ResultObject() {
        }

        public long getId() {
            return id;
        } 
        
        public void setId(long id) {
            this.id = id;
        }
    }
}
