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
    private static String VALID_MSG = "";
    private static String INVALID_FIRST_NAME_MSG = "first name submitted is not valid.";
    private static String INVALID_LAST_NAME_MSG = "last name submitted is not valid.";
    private static String INVALID_SUBJECT_MSG = "subject is not valid.";
    private static String INVALID_MESSAGE_CONTENT = "message content cnnot be empty or contain html.";
    private static String INVALID_USER_EMAIL_MSG = "user's email is not valid.";
    private static String INVALID_USER_PHONE = "user's phone is not valid.";
    private static String INVALID_MESSAGE_CONTENT_LENGTH = "Message contant cannot be longer than 4000 characters.";

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    @Override
    public String toString() {
        return String.join("\n", "First Name: " + firstName, "Last Name: " + lastName, "Email: " + email, "Phone" + phone,
                "Inquiry: " + userMessage);
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
            message = String.join(lineSeparator, INVALID_USER_EMAIL_MSG);
        }
        HTMLTagValidator htmlTagValidator = new HTMLTagValidator();
        if (StringUtils.isBlank(firstName) || htmlTagValidator.hasHTMLTags(firstName)) {
            message = String.join(lineSeparator, INVALID_FIRST_NAME_MSG);
        }
        if (StringUtils.isBlank(lastName) || htmlTagValidator.hasHTMLTags(firstName)) {
            message = String.join(lineSeparator, INVALID_LAST_NAME_MSG);
        }
        if (!StringUtils.isBlank(phone) && !PhoneNumberValidator.isValid(phone)) {
            message = String.join(lineSeparator, INVALID_USER_PHONE);
        }
        if (StringUtils.isBlank(userMessage) || htmlTagValidator.hasHTMLTags(userMessage)) {
            message = String.join(lineSeparator, INVALID_MESSAGE_CONTENT);
        }
        if (userMessage.length() > 4000) {
            message = String.join(lineSeparator, INVALID_MESSAGE_CONTENT_LENGTH);
        }
        if (StringUtils.isBlank(subject) || htmlTagValidator.hasHTMLTags(subject)) {
            message = String.join(lineSeparator, INVALID_SUBJECT_MSG);
        }

        return message;
    }

}
