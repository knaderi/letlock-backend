/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
/**
 * 
 */
package com.landedexperts.letlock.filetransfer.backend;

import java.util.Properties;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.landedexperts.letlock.filetransfer.backend.utils.LetLockBackendEnv;

/**
 * @author knaderi
 *
 */
@Configuration
public class MailConfiguration {
    

    @Value("${spring.mail.host}")
    private String mailHost;
    
    @Value("${spring.mail.port}")
    private String mailPort;    
    
    
    @Value("${spring.profiles.active}")
    private String env; 
    
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String tlsEnabled;
        
    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private String tlsRequired;
    
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String smtpAuth;
    
    @Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
    private String connectionTimeOut;
    
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private String smtpTimeOut;
    
    @Value("${spring.mail.properties.mail.smtp.writetimeout}")
    private String writeTimeOut;
   
    @Value("${spring.mail.properties.mail.transport.protocol}")
    private String transportProtocol;
    
    
    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        LetLockBackendEnv constants = LetLockBackendEnv.getInstance(env);
        if(constants.isLocalEnv()) {
            mailSender.setUsername( "letlock-notifications@landedexperts.com");
            mailSender.setPassword("7JqbmMu8ddAz3uS2YpNIWcjz7aE!");
        }else {
            Properties mailProperties = AWSSecretManagerFacade.getSpringMailProperties(constants.getEnv());
            mailSender.setUsername(mailProperties.getProperty(constants.SECRET_SPRING_MAIL_USERNAME_KEY));
            mailSender.setPassword(mailProperties.getProperty(constants.SECRET_SPRING_MAIL_PASSWORD_KEY));
        }
        mailSender.setHost(mailHost);
        mailSender.setPort(Integer.valueOf(mailPort));

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", smtpAuth);
        javaMailProperties.put("mail.transport.protocol", transportProtocol);
        javaMailProperties.put("mail.smtp.starttls.required", tlsEnabled);
        javaMailProperties.put("mail.smtp.starttls.enable", tlsRequired);
        javaMailProperties.put("mail.smtp.connectiontimeout", connectionTimeOut);
        javaMailProperties.put("mail.smtp.timeout", smtpTimeOut);
        javaMailProperties.put("mail.smtp.writetimeout", writeTimeOut);


        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

}
