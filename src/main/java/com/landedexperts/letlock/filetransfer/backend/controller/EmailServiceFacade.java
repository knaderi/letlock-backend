/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.landedexperts.letlock.filetransfer.backend.service.Email;
import com.landedexperts.letlock.filetransfer.backend.service.LetLockEmailService;
import com.landedexperts.letlock.filetransfer.backend.utils.LetLockBackendEnv;

@Service
public class EmailServiceFacade {

    @Autowired
    LetLockEmailService letLockEmailService;

    private final Logger logger = LoggerFactory.getLogger(EmailServiceFacade.class);

    private static String FORGOT_PASSWORD_EMAIL_SUBJECT = "Reset Your Password";
    private static String CONFIRM_SIGNUP_EMAIL_SUBJECT = "Confirm Your Email";
    private static String CHANGE_PASSWORD_EMAIL_SUBJECT = "Your password has changed";
    static String USER_CONFIRM_TOKEN = "%USER_CONFIRM_TOKEN%";
    static String EMAIL_TOKEN = "%EMAIL_TOKEN%";
    static String VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN = "%VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN%";
    static String LETLOCK_LOGO_URL_TOKEN = "%LETLOCK_LOGO_URL_TOKEN%";
    static String LETLOCK_FOOTER_LOGO_TOKEN = "%LETLOCK_FOOTER_LOGO_TOKEN%";

    @Value("${validate.reset.password.token.url}")
    String validateResetPasswordTokenURL;

    @Value("${validate.confirm.signup.url}")
    String confirmSignupURL;

    @Value("${nonprod.receipient.email}")
    private String nonProdReceipientEmail;

    @Value("${nonprod.email.enabled}")
    private String nonProdEmailActive;

    @Value("${letlock.notifications.email}")
    private String letlockNotificationEmail;

    @Value("${forgot.password.email.template}")
    private String forgotPasswordEmailTemplate;
    
    @Value("${change.password.email.template}")
    private String changePasswordEmailTemplate;

    @Value("${confirm.signup.email.template}")
    private String confirmSignupEmailTemplate;

    @Value("${letlock.logo.url}")
    String letlockLogoURL;

    @Value("${letlock.footer.logo.url}")
    String letlockFooterLogoURL;
    
    @Value("${letlock.contactus.recipient.email}")
    private String letlockContactUsRecipientEmail;

    void sendForgotPasswordHTMLEmail(String recepientEmail, String resetEmailToken) throws Exception {

        LetLockBackendEnv constants = LetLockBackendEnv.getInstance();
        if ("prd".equals(constants.getEnv()) || "true".contentEquals(nonProdEmailActive)) {
            Email email = new Email();
            email.setFrom(letlockNotificationEmail);

            if ("prd".equals(constants.getEnv())) {
                email.setTo(recepientEmail);
            } else if ("true".contentEquals(nonProdEmailActive)) {
                logger.info("replacing recipient email: " + recepientEmail);
                email.setTo(nonProdReceipientEmail);
            }

            email.setSubject(FORGOT_PASSWORD_EMAIL_SUBJECT);
            email.setMessageText(getForgotPasswordHTMLEmailBody(resetEmailToken, recepientEmail));
            letLockEmailService.sendHTMLMail(email);
        } else {
            logger.info("Email service is disabled in properties file.");
        }
    }

    String getForgotPasswordHTMLEmailBody(String resetEmailToken, String confirmEmailToken) throws Exception {
        String emailBody = readForgotPasswordEmailBody();
        emailBody = emailBody.replace(VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN, validateResetPasswordTokenURL)
                .replace(USER_CONFIRM_TOKEN, resetEmailToken)
                .replace(EMAIL_TOKEN, confirmEmailToken)
                .replace(LETLOCK_LOGO_URL_TOKEN, letlockLogoURL)
                .replace(LETLOCK_FOOTER_LOGO_TOKEN, letlockFooterLogoURL);
        return emailBody;
    }

    String readForgotPasswordEmailBody() throws IOException {
        URL url = Resources.getResource(forgotPasswordEmailTemplate);
        String emailBody = Resources.toString(url, Charsets.UTF_8);
        return emailBody;
    }
    
    String getChangePasswordHTMLEmailBody() throws IOException {
        URL url = Resources.getResource(changePasswordEmailTemplate);
        String emailBody = Resources.toString(url, Charsets.UTF_8);
        emailBody = emailBody
                .replace(LETLOCK_LOGO_URL_TOKEN, letlockLogoURL)
                .replace(LETLOCK_FOOTER_LOGO_TOKEN, letlockFooterLogoURL);
        return emailBody;
    }

    String getConfirmSignupHTMLEmailBody(String resetEmailToken, String recipientEmail) throws Exception {
        logger.info("recipientEmail: " + recipientEmail);
        String emailBody = readConfirmSignupdEmailBody();
        emailBody = emailBody.replace(VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN, confirmSignupURL)
                .replace(USER_CONFIRM_TOKEN, resetEmailToken)
                .replace(EMAIL_TOKEN, recipientEmail)
                .replace(LETLOCK_LOGO_URL_TOKEN, letlockLogoURL)
                .replace(LETLOCK_FOOTER_LOGO_TOKEN, letlockFooterLogoURL);
        return emailBody;
    }

    String readConfirmSignupdEmailBody() throws IOException {
        URL url = Resources.getResource(confirmSignupEmailTemplate);
        String emailBody = Resources.toString(url, Charsets.UTF_8);
        return emailBody;
    }

    void sendConfirmSignupHTMLEmail(String recipientEmail, String resetEmailToken) throws Exception {
        logger.info("sendConfirmSignupHTMLEmail, recipientEmail: " + recipientEmail);
        LetLockBackendEnv constants = LetLockBackendEnv.getInstance();
        if ("prd".equals(constants.getEnv()) || "true".contentEquals(nonProdEmailActive)) {
            Email email = new Email();
            email.setFrom(letlockNotificationEmail);

            if ("prd".equals(constants.getEnv())) {
                email.setTo(recipientEmail);
            } else if ("true".contentEquals(nonProdEmailActive)) {
                logger.info("replacing recipient email: " + recipientEmail);
                email.setTo(nonProdReceipientEmail);
            }

            email.setSubject(CONFIRM_SIGNUP_EMAIL_SUBJECT);
            email.setMessageText(getConfirmSignupHTMLEmailBody(resetEmailToken, recipientEmail));
            logger.info(resetEmailToken + ", " + recipientEmail);
            letLockEmailService.sendHTMLMail(email);
        } else {
            logger.info("Email service is disabled in properties file.");
        }
    }

    /**
     * @param contactUsModel
     */
    public void sendContactUsEmail(ContactUsModel contactUsModel) throws Exception  {
        Email email = new Email();
        email.setFrom(letlockNotificationEmail);
        email.setSubject(contactUsModel.getSubject());
        email.setMessageText(contactUsModel.getMessage());
        email.setTo(letlockContactUsRecipientEmail);
        letLockEmailService.sendMail(email);
    }

    public void sendChangePasswordEmail(String recepientEmail)  throws Exception  {
        LetLockBackendEnv constants = LetLockBackendEnv.getInstance();
        if ("prd".equals(constants.getEnv()) || "true".contentEquals(nonProdEmailActive)) {
            Email email = new Email();
            email.setFrom(letlockNotificationEmail);

            if ("prd".equals(constants.getEnv())) {
                email.setTo(recepientEmail);
            } else if ("true".contentEquals(nonProdEmailActive)) {
                logger.info("replacing recipient email: " + recepientEmail);
                email.setTo(nonProdReceipientEmail);
            }
            email.setSubject(CHANGE_PASSWORD_EMAIL_SUBJECT);
            email.setMessageText(getChangePasswordHTMLEmailBody());
            letLockEmailService.sendHTMLMail(email);
        } else {
            logger.info("Email service is disabled in properties file.");
        }
        
    }

}
