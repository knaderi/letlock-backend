package com.landedexperts.letlock.filetransfer.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landedexperts.letlock.filetransfer.backend.service.Email;
import com.landedexperts.letlock.filetransfer.backend.service.LetLockEmailService;

@Service
public class EmailServiceFacade {
    
    @Autowired
    LetLockEmailService letLockEmailService;

    
    private static String FORGOT_PASSWORD_FROM_EMAIL="letlock-notifications@landedexperts.com";
    private static String FORGOT_PASSWORD_EMAIL_SUBJECT="Reset Your Password";
    void sendForgotPasswordEmail(String recepientEmail, String resetEmailToken) {
        
        Email email = new Email();
        email.setFrom(FORGOT_PASSWORD_FROM_EMAIL);
        email.setTo(recepientEmail);
        email.setSubject(FORGOT_PASSWORD_EMAIL_SUBJECT);
        email.setMessageText(getForgotPasswordEmailBody());
        letLockEmailService.sendMail(email);
    }
    
 
 
    
    private String getForgotPasswordEmailBody() {
     return "This is the email body";   
    }
    
  

}
