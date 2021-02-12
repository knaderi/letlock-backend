package com.landedexperts.letlock.filetransfer.backend.controller;

import javax.servlet.http.HttpServletRequest;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;
import com.landedexperts.letlock.filetransfer.backend.utils.LetLockAuthTokenValidationException;


public class BaseController {

    long mapToUserId(final String token) throws LetLockAuthTokenValidationException {
        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            return userId;
        }
        throw new LetLockAuthTokenValidationException();

    }
    
    String getToken(final HttpServletRequest request) throws Exception {
        return request.getHeader("Authorization").replace("Bearer ", "");

    }

}
