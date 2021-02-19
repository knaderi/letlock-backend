package com.landedexperts.letlock.filetransfer.backend.utils;

public enum ResponseCode {
    TOKEN_INVALID ("TOKEN_INVALID","Invalid token"),
    ADMIN_USER_EXPECTED ("ADMIN_USER_EXPECTED","Admin user is required to perform this action");
    
    private final String code;
    private final String message;
    
    ResponseCode(String code, String message) {
        this.message = message;
        this.code = code;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public static ResponseCode fromCode(String code) {
        for (ResponseCode e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
