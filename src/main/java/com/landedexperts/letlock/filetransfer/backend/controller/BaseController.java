package com.landedexperts.letlock.filetransfer.backend.controller;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    String getToken(final HttpServletRequest request) throws Exception {
        return request.getHeader("Authorization").replace("Bearer ", "");

    }

}
