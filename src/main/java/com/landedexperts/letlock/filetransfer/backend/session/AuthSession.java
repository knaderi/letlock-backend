/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Irina Soboleva - 2021
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.session;

import java.util.Date;

import com.landedexperts.letlock.filetransfer.backend.utils.Verify2FACodeResult;

public class AuthSession {
    private static long timeout = 3600000L; /*Timeout in milliseconds, 60min*/
    private static Date newExpire() {
        return new Date((new Date()).getTime() + AuthSession.timeout);
    }
    

    private long userId;
    private Date expires;
    private String code;
    private int attempts = 0;

    public AuthSession(final long userId) {
        this.userId = userId;
        this.expires = AuthSession.newExpire();
    }
    
    public void setCode(String code) {
        this.code = code;
        attempts = 0;
        extend();
    }
    
    public long getUserId() {
        return userId;
    }
    
    public Verify2FACodeResult verifyCode(String code) {
        attempts++;
        Boolean valid = code.equals(this.code);
        return new Verify2FACodeResult(valid, attempts);
    }

    public Boolean isActive() {
        /*
         * The UserSession is active when the
         * current time is before the expiring date
         * */
        return (new Date()).before(this.expires);
    }

    public void extend() {
        this.expires = AuthSession.newExpire();
    }
}
