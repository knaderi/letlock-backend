/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

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
import com.landedexperts.letlock.filetransfer.backend.utils.RequestData;

@Service
public class EmailServiceFacade {

    private static final String PARTNER_NAME = "%PARTNER_NAME%";

    private static final String TRY_LETLOCK_URL_TOKEN = "%TRY_LETLOCK_URL_TOKEN%";

    @Autowired
    LetLockEmailService letLockEmailService;

    private final Logger logger = LoggerFactory.getLogger(EmailServiceFacade.class);

    private static String FORGOT_PASSWORD_EMAIL_SUBJECT = "Reset Your Password";
    private static String CONFIRM_SIGNUP_EMAIL_SUBJECT = "Confirm Your Email";
    private static String WELCOME_TO_LETLOCK_FREE_CREDIT = "Welcome to Letlock file transfer!";
    private static String LETLOCK_REGISTERATION_CONFIRMATION_SUBJECT = "New user confirmed signup email!";
    private static String CHANGE_PASSWORD_EMAIL_SUBJECT = "Your password has changed";
    static String USER_CONFIRM_TOKEN = "%USER_CONFIRM_TOKEN%";
    static String EMAIL_TOKEN = "%EMAIL_TOKEN%";
    static String VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN = "%VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN%";
    static String CONFIRM_SIGNUP_URL = "%CONFIRM_SIGNUP_URL%";
    static String LETLOCK_LOGO_URL_TOKEN = "%LETLOCK_LOGO_URL_TOKEN%";
    static String LETLOCK_FOOTER_LOGO_TOKEN = "%LETLOCK_FOOTER_LOGO_TOKEN%";
    static String EMAIL = "%EMAIL%";
    static String TOKEN = "%TOKEN%";

    @Value("${validate.reset.password.token.url}")
    String validateResetPasswordTokenURL;

    @Value("${validate.confirm.signup.token.url}")
    String confirmSignupURL;

    @Value("${nonprod.receipient.email}")
    private String nonProdReceipientEmail;

    @Value("${nonprod.email.enabled}")
    private String nonProdEmailActive;

    @Value("${letlock.notifications.sender.email}")
    private String letlockNotificationEmail;

    @Value("${forgot.password.email.template}")
    private String forgotPasswordEmailTemplate;
    
    @Value("${change.password.email.template}")
    private String changePasswordEmailTemplate;

    @Value("${confirm.signup.email.template}")
    private String confirmSignupEmailTemplate;
    
    @Value("${confirm.via.partner.signup.email.template}")
    private String confirmViaPartnerSignupEmailTemplate;
    
    @Value("${letlock.welcome.with.free.credit.email.template}")
    private String welcomeWithFreeCreditEmailTemplate;

    @Value("${letlock.logo.url}")
    String letlockLogoURL;

    @Value("${letlock.footer.logo.url}")
    String letlockFooterLogoURL;
    
    @Value("${letlock.contactus.recipient.email}")
    private String letlockContactUsRecipientEmail;

    @Value("${try.letlock.token.url}")
    private String tryLetlockURL;
    
    @Value("${letlock.welcome.free.credit.email}")
    private String freeCreditWelcomeEmail;
    
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

    String getForgotPasswordHTMLEmailBody(String resetEmailToken, String recepientEmail) throws Exception {
        String emailBody = readForgotPasswordEmailBody();
        emailBody = emailBody.replace(VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN, validateResetPasswordTokenURL)
                .replace(USER_CONFIRM_TOKEN, resetEmailToken)
                .replace(EMAIL, URLEncoder.encode(recepientEmail,"UTF8"))
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
                .replace(EMAIL_TOKEN, URLEncoder.encode(recipientEmail,"UTF8"))
                .replace(LETLOCK_LOGO_URL_TOKEN, letlockLogoURL)
                .replace(LETLOCK_FOOTER_LOGO_TOKEN, letlockFooterLogoURL);
        return emailBody;
    }
    
    String getConfirmViaPartnerSignupHTMLEmailBody(String resetEmailToken, String recipientEmail, String partnerName) throws Exception {
        logger.info("recipientEmail: " + recipientEmail);
        URL url = Resources.getResource(confirmViaPartnerSignupEmailTemplate);
        String emailBody = Resources.toString(url, Charsets.UTF_8);
        emailBody = emailBody.replace(CONFIRM_SIGNUP_URL, confirmSignupURL)
                .replace(USER_CONFIRM_TOKEN, resetEmailToken)
                .replace(EMAIL_TOKEN, URLEncoder.encode(recipientEmail,"UTF8"))
                .replace(LETLOCK_LOGO_URL_TOKEN, letlockLogoURL)
                .replace(LETLOCK_FOOTER_LOGO_TOKEN, letlockFooterLogoURL)
                .replace(PARTNER_NAME, partnerName.toUpperCase());
         //TODO Add partner logo URL to this aas well. Currently Appsumo is hardcoded.
        return emailBody;
    }
    
        
    String getViaPartnerConfirmSignupHTMLEmailBody(String resetEmailToken, String recipientEmail) throws Exception {
        logger.info("recipientEmail: " + recipientEmail);
        String emailBody = readConfirmSignupdEmailBody();
        emailBody = emailBody.replace(VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN, confirmSignupURL)
                .replace(USER_CONFIRM_TOKEN, resetEmailToken)
                .replace(EMAIL_TOKEN, URLEncoder.encode(recipientEmail,"UTF8"))
                .replace(LETLOCK_LOGO_URL_TOKEN, letlockLogoURL)
                .replace(LETLOCK_FOOTER_LOGO_TOKEN, letlockFooterLogoURL);
        return emailBody;
    }
    
    String getWelcomeWithFreeCreditHTMLEmailBody(String recipientEmail, String downloadTokenURL) throws Exception {
        logger.info("recipientEmail: " + recipientEmail);
        String emailBody = readWelcomeWithFreeCreditHTMLEmailBody();
        emailBody = emailBody.replace(VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN, confirmSignupURL)
                .replace(TRY_LETLOCK_URL_TOKEN, downloadTokenURL)
                .replace(EMAIL_TOKEN, URLEncoder.encode(recipientEmail,"UTF8"))
                .replace(LETLOCK_LOGO_URL_TOKEN, letlockLogoURL)
                .replace(LETLOCK_FOOTER_LOGO_TOKEN, letlockFooterLogoURL);
        return emailBody;
    }

    String readConfirmSignupdEmailBody() throws IOException {
        URL url = Resources.getResource(confirmSignupEmailTemplate);
        String emailBody = Resources.toString(url, Charsets.UTF_8);
        return emailBody;
    }
    
    String readWelcomeWithFreeCreditHTMLEmailBody() throws IOException {
        URL url = Resources.getResource(welcomeWithFreeCreditEmailTemplate);
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
                logger.info("replacing recipient email: " + recipientEmail + "with " + nonProdReceipientEmail);
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
    
    void sendConfirmViaPartnerSignupHTMLEmail(String recipientEmail, String resetEmailToken, String partnerName) throws Exception {
        logger.info("sendConfirmViaPartnerSignupHTMLEmail, recipientEmail: " + recipientEmail);
        LetLockBackendEnv constants = LetLockBackendEnv.getInstance();
        if ("prd".equals(constants.getEnv()) || "true".contentEquals(nonProdEmailActive)) {
            Email email = new Email();
            email.setFrom(letlockNotificationEmail);

            if ("prd".equals(constants.getEnv())) {
                email.setTo(recipientEmail);
            } else if ("true".contentEquals(nonProdEmailActive)) {
                logger.info("replacing recipient email: " + recipientEmail + "with " + nonProdReceipientEmail);
                email.setTo(nonProdReceipientEmail);
            }
            
            email.setSubject(CONFIRM_SIGNUP_EMAIL_SUBJECT);
            email.setMessageText(getConfirmViaPartnerSignupHTMLEmailBody(resetEmailToken, recipientEmail, partnerName));
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

    public void sendWelcomeWithFreeCreditEmail(String recipientEmail)  throws Exception   {
        logger.info("sendWelcomeWithFreeCreditEmail, recipientEmail: " + recipientEmail);
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

            email.setSubject(WELCOME_TO_LETLOCK_FREE_CREDIT);
            email.setMessageText(getWelcomeWithFreeCreditHTMLEmailBody(recipientEmail,tryLetlockURL));
            letLockEmailService.sendHTMLMail(email);
        } else {
            logger.info("Email service is disabled in properties file.");
        }
    }
    
    
    public void sendAdminRegistrationNotification(String recipientEmail, RequestData requestData) throws Exception {
        LetLockBackendEnv constants = LetLockBackendEnv.getInstance();
        if ("prd".equals(constants.getEnv()) || "true".contentEquals(nonProdEmailActive)) {
            Email email = new Email();
            email.setFrom(letlockNotificationEmail);

            if ("prd".equals(constants.getEnv())) {
                email.setTo(letlockContactUsRecipientEmail);
            } else if ("true".contentEquals(nonProdEmailActive)) {
                email.setTo(nonProdReceipientEmail);
            }

            email.setSubject(LETLOCK_REGISTERATION_CONFIRMATION_SUBJECT);
            email.setMessageText("User " + recipientEmail + " has attempted registeration: " + requestData.toJSON());
            letLockEmailService.sendHTMLMail(email);
        } else {
            logger.info("Email service is disabled in properties file.");
        }
    }
    
    public void sendAdminFailureNotification(String action, String errorMessage) throws Exception {
        LetLockBackendEnv constants = LetLockBackendEnv.getInstance();
        if ("prd".equals(constants.getEnv()) || "true".contentEquals(nonProdEmailActive)) {
            Email email = new Email();
            email.setFrom(letlockNotificationEmail);

            if ("prd".equals(constants.getEnv())) {
                email.setTo(letlockContactUsRecipientEmail);
            } else if ("true".contentEquals(nonProdEmailActive)) {
                email.setTo(nonProdReceipientEmail);
            }

            email.setSubject(action + " failed");
            email.setMessageText(action + " failed with error message: " + errorMessage);
            logger.info(email.getTo());
            letLockEmailService.sendHTMLMail(email);
        } else {
            logger.info("Email service is disabled in properties file.");
        }
    }

}
