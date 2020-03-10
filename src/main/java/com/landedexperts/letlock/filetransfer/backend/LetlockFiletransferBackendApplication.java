/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.landedexperts.letlock.filetransfer.backend.utils.LetLockBackendEnv;


@SpringBootApplication
public class LetlockFiletransferBackendApplication {

    @Value("${spring.mail.host}")
    private String mailHost;
    
    @Value("${spring.mail.port}")
    private String mailPort;    
    
    
    @Value("${spring.profiles.active}")
    private String env; 
    
   
    
	@Bean
	public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*", "http://localhost:3000","http://letlockweb-dev.s3-website-us-west-2.amazonaws.com","letlockbackenddev.us-west-2.elasticbeanstalk.com");                
            }
        };
    }

	public static void main(String[] args) {
		SpringApplication.run(LetlockFiletransferBackendApplication.class, args);
	}
	
    
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
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", "true");

        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }
       

}
