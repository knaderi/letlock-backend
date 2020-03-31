/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonResponse<T> extends ReturnCodeMessageResponse {
    public void setResult(T result) {
        this._result = result;
    }

    public JsonResponse() {
        super();
    }

    private T _result = (T)"{\"type\":\"json\", \"value\":\"{}\"}";
    
    public JsonResponse(final T result, final String returnCode, final String returnMessage) {
        super(returnCode, returnMessage);
        this._result = result;
    }

    public JsonResponse(final T result) {
        super("SUCCESS", "");
        if (StringUtils.isEmpty(result)) {
            setReturnCode("NO_LOCATION_FOUND");
            setReturnMessage("No location data was retrived from db.");
        }
        this._result = result;
    }

    public T getResult() {
        return _result;
    }

    public static JsonResponse<Object> getResult(Object jsonString) throws JsonParseException, JsonMappingException, IOException {
        try {
            if (jsonString instanceof Map) {
                HashMap<String, String> hashMap = (HashMap<String, String>) jsonString;
                return  new JsonResponse<Object>(hashMap.get("value"));
            } else {
                JsonResponse response = new ObjectMapper().readValue(jsonString.toString(), JsonResponse.class);
                return (JsonResponse<Object>) getResult(response.getResult());
            }
        } catch (Exception e) {
            return new JsonResponse<Object>(jsonString, "SUCCESS", "");
        }

    }
    
//    public static JsonResponse<Object> getResult(Object jsonString) throws JsonParseException, JsonMappingException, IOException {
//        try {
//            if (jsonString instanceof Map) {
//                HashMap<String, String> hashMap = (HashMap<String, String>) jsonString;
//                return  new JsonResponse<Object>(hashMap.get("value"));
//            } else if (StringUtils.isEmpty(jsonString)){
//               return new JsonResponse(jsonString);
//            }else {
//                JsonResponse response = new ObjectMapper().readValue(jsonString.toString(), JsonResponse.class);
//                return (JsonResponse<Object>) getResult(response.getResult());
//            }
//        } catch (Exception e) {
//            return new JsonResponse<Object>(jsonString, "SUCCESS", "");
//        }
//
//    }
}
