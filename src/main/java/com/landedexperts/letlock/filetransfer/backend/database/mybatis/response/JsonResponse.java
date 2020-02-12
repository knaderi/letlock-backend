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
        _result = null;
    }

    private T _result;

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

    public static JsonResponse getResult(Object jsonString) throws JsonParseException, JsonMappingException, IOException {
        JsonResponse returnResponse = null;
        try {
            if (jsonString instanceof Map) {
                returnResponse = new JsonResponse(((HashMap) jsonString).get("value"));
            } else {
                JsonResponse response = new ObjectMapper().readValue(jsonString.toString(), JsonResponse.class);
                returnResponse = (JsonResponse) getResult(response.getResult());
            }
        } catch (Exception e) {
            returnResponse = new JsonResponse(jsonString, "SUCCESS", "");
        }
        return returnResponse;
    }
}
