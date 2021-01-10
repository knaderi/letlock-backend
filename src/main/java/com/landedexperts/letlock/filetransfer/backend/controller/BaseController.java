package com.landedexperts.letlock.filetransfer.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;
import com.landedexperts.letlock.filetransfer.backend.utils.LetLockAuthTokenValidationException;


public class BaseController {


    @GetMapping(value = "/validate_token", produces = { "application/JSON" })
    public BooleanResponse validateToken(@RequestParam(value = "token") final String token) throws Exception {

        String returnCode = "SUCCESS";
        String returnMessage = "";
        boolean validToken = true;
        try {
            mapToUserId(token);
        } catch (LetLockAuthTokenValidationException lae) {
            returnCode = "TOKEN_INVALID";
            returnMessage = "Invalid token";
            validToken = false;
        }
        return new BooleanResponse(validToken, returnCode, returnMessage);

    }

    long mapToUserId(final String token) throws LetLockAuthTokenValidationException {
        long userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            return SessionManager.getInstance().getUserId(token);
        }
        throw new LetLockAuthTokenValidationException();

    }

}
