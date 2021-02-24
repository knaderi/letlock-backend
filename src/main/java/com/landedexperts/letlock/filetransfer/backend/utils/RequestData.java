package com.landedexperts.letlock.filetransfer.backend.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

import com.paypal.base.rest.JSONFormatter;

public class RequestData {
    
    private static final String TOOL_APP = "TOOL";
    private static final String MOBILE = "MOBILE";
    private static final String PC = "PC";
    private static final String INSTALLABLE_APP = "APP";
    private static final String WEB_APP = "WEB";
    private String origin; //web or desktop
    private String appType; //application, browser
    private String ipAddress;
    private String deviceType; //pc or mobile
    private String tokenPrefix;
    private String userAgent;
    
    public String toJSON() {
        return JSONFormatter.toJSON(this);
    }

    @Override
    public String toString() {
        return toJSON();
    }
    
    public String getUserAgent() {
        return userAgent;
    }


    public String getOrigin() {
        return origin;
    }

    public RequestData(HttpServletRequest req) {
        this.ipAddress = req.getRemoteAddr();
        this.origin = req.getHeader("origin");
        this.userAgent = req.getHeader("User-Agent");
        setDeviceType();
        setAppType();
        setTokenPrefix();
    }
    
    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    private void setDeviceType() {
        if(!StringUtils.isBlank(userAgent) && (userAgent.toLowerCase().contains("android") || userAgent.toLowerCase().contains("ios"))) {
            deviceType = MOBILE;
        }else {
            deviceType = PC;
        }
    }
    
    private void setAppType() {
        if(StringUtils.isBlank(origin)) {
            appType = TOOL_APP;
        } else if(origin.toLowerCase().contains("http")) {
            appType = WEB_APP;
        }else {
            appType = INSTALLABLE_APP;
        }
    }
    
    private void setTokenPrefix() {
        tokenPrefix = new StringBuilder().append(deviceType.charAt(0)).append(appType.charAt(0)).toString();
    }
    

}
