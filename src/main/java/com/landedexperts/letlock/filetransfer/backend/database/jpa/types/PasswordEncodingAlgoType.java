package com.landedexperts.letlock.filetransfer.backend.database.jpa.types;

import java.io.Serializable;

public enum PasswordEncodingAlgoType implements Serializable{
    
    base64;
    
    public PasswordEncodingAlgoType fromValue(String code) {
        if(code.equals("base64")) {
            return PasswordEncodingAlgoType.base64;
        }else {
            return null;
        }
    }
}
