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

@Service
public class EmailServiceFacade {

    @Autowired
    LetLockEmailService letLockEmailService;
    
    private final Logger logger = LoggerFactory.getLogger(EmailServiceFacade.class);

    private static String FORGOT_PASSWORD_EMAIL_SUBJECT = "Reset Your Password";
    static String RESET_TOKEN = "%RESET_TOKEN%";
    static String VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN = "%VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN%";
    static String LETLOCK_LOGO_URL_TOKEN = "%LETLOCK_LOGO_URL_TOKEN%";
    static String LETLOCK_FOOTER_LOGO_TOKEN = "%LETLOCK_FOOTER_LOGO_TOKEN%";

    @Value("${validate.reset.password.token.url}")
    String validateResetPasswordTokenURL;
    
    @Value("${spring.profiles.active}")
    private String env;
    
    @Value("${nonprod.receipient.email}")
    private String nonProdReceipientEmail;

    @Value("${letlock.notifications.email}")
    private String letlockNotificationEmail;
    
    @Value("${forgot.password.email.template}")
    private String forgotPasswordEmailTemplate;
    
    @Value("${letlock.logo.url}")
    String letlockLogoURL;
    
    @Value("${letlock.footer.logo.url}")
    String letlockFooterLogoURL;
    
    void sendForgotPasswordHTMLEmail(String recepientEmail, String resetEmailToken) throws Exception {

        Email email = new Email();
        email.setFrom(letlockNotificationEmail);
        
        if("prd".equals(env)) {
            email.setTo(recepientEmail);            
        }else {
            logger.info("replacing recipient email: " + recepientEmail);
            email.setTo(nonProdReceipientEmail);   
        }
             
        email.setSubject(FORGOT_PASSWORD_EMAIL_SUBJECT);
        email.setMessageText(getForgotPasswordHTMLEmailBody(resetEmailToken));
        letLockEmailService.sendHTMLMail(email);
    }

    String getForgotPasswordHTMLEmailBody(String resetEmailToken) throws Exception {
        String emailBody = readEmailBody();
        emailBody = emailBody.replace(VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN, validateResetPasswordTokenURL)
        .replace(RESET_TOKEN, resetEmailToken)
        .replace(LETLOCK_LOGO_URL_TOKEN, letlockLogoURL)
        .replace(LETLOCK_FOOTER_LOGO_TOKEN, letlockFooterLogoURL);
        return emailBody;
    }

    String readEmailBody() throws IOException {
        URL url = Resources.getResource(forgotPasswordEmailTemplate);
        String emailBody = Resources.toString(url, Charsets.UTF_8);
        return emailBody;
    }

}
