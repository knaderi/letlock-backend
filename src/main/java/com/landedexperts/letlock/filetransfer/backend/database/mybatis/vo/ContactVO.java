/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Irina Soboleva - 2020
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

public class ContactVO {

    private String contactUserName = "";
    private String contactLabel = "";

    public ContactVO() {
        
    }
   
    public ContactVO(String contactUserName, String contactLabel) {
        this.contactUserName = contactUserName;
        this.contactLabel = contactLabel;
    }

    public String getContactUserName() {
        return contactUserName;
    }

    public void setContactUserName(String contactUserName) {
        this.contactUserName = contactUserName;
    }

    public String getContactLabel() {
        return contactLabel;
    }

    public void setContactLabel(String contactLabel) {
        this.contactLabel = contactLabel;
    }

}
