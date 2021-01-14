/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import java.util.Date;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public class UserVO extends ReturnCodeMessageResponse{

    
    private String loginName;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    private String email;


    private String password;
    
    
    private String passwordHashingAlgo;
    
    
    private String passwordEncodingAlgo;

    
    private UserStatusType status;

    
    private Date statusDate;

    
    private Date createdDate;


    private Date updatedDate;


    private Date lastLogin;


    private String resetToken;
    
    
    private Boolean twoFAEnabled;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHashingAlgo() {
        return passwordHashingAlgo;
    }

    public void setPasswordHashingAlgo(String passwordHashingAlgo) {
        this.passwordHashingAlgo = passwordHashingAlgo;
    }

    public String getPasswordEncodingAlgo() {
        return passwordEncodingAlgo;
    }


    public void setPasswordEncodingAlgo(String passwordEncodingAlgo) {
        this.passwordEncodingAlgo = passwordEncodingAlgo;
    }

    public UserStatusType getStatus() {
        return status;
    }

    public void setStatus(UserStatusType status) {
        this.status = status;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public Date isCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    
    public Boolean getTwoFAEnabled() {
        return twoFAEnabled;
    }
    
    public void setTwoFAEnabled(Boolean twoFAEnabled) {
        this.twoFAEnabled = twoFAEnabled;
    }

}
