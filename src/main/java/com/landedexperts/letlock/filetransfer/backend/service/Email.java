/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.service;

public class Email {
    private String from;
    private String to;
    private String subject;
    private String messageText;
    public Email() {
    }
    public String getFrom() {
       return from;
    }
    public void setFrom(String from) {
       this.from = from;
    }
    public String getTo() {
       return to;
    }
    public void setTo(String to) {
       this.to = to;
    }
    public String getSubject() {
       return subject;
    }
    public void setSubject(String subject) {
       this.subject = subject;
    }
    public String getMessageText() {
       return messageText;
    }
    public void setMessageText(String messageText) {
       this.messageText = messageText;
    }
 }
