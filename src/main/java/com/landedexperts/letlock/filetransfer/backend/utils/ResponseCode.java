package com.landedexperts.letlock.filetransfer.backend.utils;

public enum ResponseCode {
    SUCCESS ("SUCCESS", ""),
    TOKEN_INVALID ("TOKEN_INVALID","Invalid token"),
    ADMIN_USER_EXPECTED ("ADMIN_USER_EXPECTED","Admin user is required to perform this action"),
    CODE_INVALID ("CODE_INVALID", "Verification code invalid"),
    TOO_MANY_ATTEMPTS ("TOO_MANY_ATTEMPTS", "Wrong verification code was entered too many times");
    
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
