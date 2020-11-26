/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Irina Soboleva - 2020
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.ContactVO;

public class ContactResponse extends ReturnCodeMessageResponse {
    private List<ContactVO> result = new ArrayList<ContactVO>();
    
    public ContactResponse() {
        super();
    }

    public ContactResponse(final String result, final String returnCode, final String returnMessage) throws IOException {
        super(returnCode, returnMessage);
        setResult(result);
    
    }
   
    public List<ContactVO> getResult() {
        return this.result;
    }
    
    public void setResult(String result) throws IOException {
        this.result = new ObjectMapper().readValue(result, new TypeReference<List<ContactVO>>(){});
    }

}
