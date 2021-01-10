package com.landedexperts.letlock.filetransfer.backend.utils;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public class LetLockAuthTokenValidationException extends Exception{

    private static final long serialVersionUID = -5377608065971840038L;
    ReturnCodeMessageResponse authErrorObj = new ReturnCodeMessageResponse("TOKEN_INVALID", "The token is not valid.");

    public LetLockAuthTokenValidationException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public LetLockAuthTokenValidationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
        // TODO Auto-generated constructor stub
    }

    public LetLockAuthTokenValidationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

    public LetLockAuthTokenValidationException(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public LetLockAuthTokenValidationException(Throwable arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

}
