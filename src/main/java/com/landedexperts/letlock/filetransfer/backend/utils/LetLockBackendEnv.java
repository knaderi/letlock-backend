/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
/**
 * 
 */
package com.landedexperts.letlock.filetransfer.backend.utils;

/**
 * This is a singleton class instantiated by LetLockApplication class upon start
 * up. It simply encapsulates and centralizes some common keys and constants
 * related to the environment the application is running on.
 * 
 * @author knaderi
 *
 */
public class LetLockBackendEnv {

    private static LetLockBackendEnv instance = null;

    // Email props secret keys
    public final String SECRET_SPRING_MAIL_PASSWORD_KEY = "SPRING_MAIL_PASSWORD";
    public final String SECRET_SPRING_MAIL_USERNAME_KEY = "SPRING_MAIL_USERNAME";

    // Database props secret keys
    public final String SECRET_DS_PASSWORD_SECRET_KEY = "password";
    public final String SECRET_DS_USER_SECRET_KEY = "username";
    public final String SECRET_DS_HOST_SECRET_KEY = "host";

    // Paypal props secret keys
    public final String SECRET_PAYPAL_CLIENT_ID_KEY = "PAYPAL_CLIENT_ID";
    public final String SECRET_PAYPAL_CLIENT_SECRET_KEY = "PAYPAL_CLIENT_SECRET";
    public final String SECRET_DS_PORT_SECRET_KEY = "port";

    // Environment related constants
    public final String LOCAL_ENV_NAME = "local";
    public final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    private String env;

    private LetLockBackendEnv() {
        super();
    }

    public static LetLockBackendEnv getInstance(String env) {
        if (instance == null) {
            instance = new LetLockBackendEnv();
            instance.env = env;
        }
        return instance;

    }

    public static LetLockBackendEnv getInstance() {
        if (instance == null) {
            throw new RuntimeException("The instance LetLockBackendEnv should have been initialized upon application startup");
        }
        return instance;
    }

    public boolean isLocalEnv() {
        return LOCAL_ENV_NAME.equals(getEnv());
    }

    public String getEnv() {
        String returnValue = null != env ? env : System.getProperty(SPRING_PROFILES_ACTIVE);
        return returnValue;
    }

}
