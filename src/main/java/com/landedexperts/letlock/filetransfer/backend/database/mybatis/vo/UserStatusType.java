/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import java.io.Serializable;

public enum UserStatusType implements Serializable{
        active,
    pending;
    
    
    public String valueOf(UserStatusType status) {
        if(status==active) {
         return "active";
        }else if(status==pending) {
            return "pending";
        }else {
            return null;
        }
    }
    

    public UserStatusType fromValue(String status) {
        if("active".contentEquals(status)) {
         return active;
        }else if("pending".contentEquals(status)) {
            return pending;
        }else {
            return null;
        }
    }

}
