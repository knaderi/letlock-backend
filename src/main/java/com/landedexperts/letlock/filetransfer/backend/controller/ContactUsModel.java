/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
/**
 * 
 */
package com.landedexperts.letlock.filetransfer.backend.controller;

import org.apache.commons.lang3.StringUtils;

import com.landedexperts.letlock.filetransfer.backend.utils.EmailValidator;
import com.landedexperts.letlock.filetransfer.backend.utils.HTMLTagValidator;
import com.landedexperts.letlock.filetransfer.backend.utils.PhoneNumberValidator;

/**
 * @author knaderi
 *
 */
public class ContactUsModel {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String subject;

    private String userMessage;
    static String VALID_MSG = "VALID";
    static String INVALID_FIRST_NAME_MSG = "First name submitted cannot be empty or contain html.";
    static String INVALID_LAST_NAME_MSG = "Last name submitted cannot be empty or contain html.";
    static String INVALID_SUBJECT_MSG = "Subject submitted cannot be empty or contain html.";
    static String INVALID_MESSAGE_CONTENT = "Message content cannot be empty or contain html.";
    static String INVALID_USER_EMAIL_MSG = "User's email is not valid.";
    static String INVALID_USER_PHONE = "User's phone is not valid.";
    static String INVALID_MESSAGE_CONTENT_LENGTH = "Message content cannot be longer than 4000 characters.";

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    @Override
    public String toString() {
        return String.join("First Name: " + firstName, " Last Name: " + lastName, " Email: " + email, " Phone: " + phone,
                " Inquiry: " + userMessage);
    }

    public String getEmailMessage() {
        return this.toString();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return userMessage;
    }

    public void setMessage(String message) {
        this.userMessage = message;
    }

    public String validate() {
        String message = VALID_MSG;
        String lineSeparator = System.lineSeparator();
        if (!new EmailValidator().isValid(email)) {
            message = message.equals(VALID_MSG) ? String.join(lineSeparator, INVALID_USER_EMAIL_MSG)
                    : String.join(lineSeparator, message, INVALID_USER_EMAIL_MSG);
        }
        HTMLTagValidator htmlTagValidator = new HTMLTagValidator();
        if (StringUtils.isBlank(firstName) || htmlTagValidator.hasHTMLTags(firstName)) {
            message = message.equals(VALID_MSG) ? String.join(lineSeparator, INVALID_FIRST_NAME_MSG)
                    : String.join(lineSeparator, message, INVALID_FIRST_NAME_MSG);
        }
        if (StringUtils.isBlank(lastName) || htmlTagValidator.hasHTMLTags(lastName)) {
            message = message.equals(VALID_MSG) ? String.join(lineSeparator, INVALID_LAST_NAME_MSG)
                    : String.join(lineSeparator, message, INVALID_LAST_NAME_MSG);
        }
        if (!StringUtils.isBlank(phone) && !PhoneNumberValidator.isValid(phone)) {
            message = message.equals(VALID_MSG) ? String.join(lineSeparator, INVALID_USER_PHONE)
                    : String.join(lineSeparator, message, INVALID_USER_PHONE);
        }
        if (StringUtils.isBlank(userMessage) || htmlTagValidator.hasHTMLTags(userMessage)) {
            message = message.equals(VALID_MSG) ? String.join(lineSeparator, INVALID_MESSAGE_CONTENT)
                    : String.join(lineSeparator, message, INVALID_MESSAGE_CONTENT);
        }
        if (!StringUtils.isBlank(userMessage) && userMessage.length() > 4000) {
            message = message.equals(VALID_MSG) ? String.join(lineSeparator, INVALID_MESSAGE_CONTENT_LENGTH)
                    : String.join(lineSeparator, message, INVALID_MESSAGE_CONTENT_LENGTH);
        }
        if (StringUtils.isBlank(subject) || htmlTagValidator.hasHTMLTags(subject)) {
            message = message.equals(VALID_MSG) ? String.join(lineSeparator, INVALID_SUBJECT_MSG)
                    : String.join(lineSeparator, message, INVALID_SUBJECT_MSG);
        }

        return message;
    }

}
