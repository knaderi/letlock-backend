package com.landedexperts.letlock.filetransfer.backend.database.jpa.types;

import java.io.Serializable;

public enum PasswordHashAlgoType implements Serializable{
    sha256;
    public PasswordHashAlgoType fromValue(String code) {
        if(code.equals("sha256")) {
            return PasswordHashAlgoType.sha256;
        }else {
            return null;
        }
    }
}
