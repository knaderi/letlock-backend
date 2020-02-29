/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend;

import java.util.Base64;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.amazonaws.services.acmpca.model.InvalidRequestException;
import com.amazonaws.services.acmpca.model.ResourceNotFoundException;
import com.amazonaws.services.applicationdiscovery.model.InvalidParameterException;
import com.amazonaws.services.datapipeline.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;


@SpringBootApplication
public class LetlockFiletransferBackendApplication {

    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private String mailPort;    
    @Value("${spring.mail.username}")
    private String userName;  
    @Value("${spring.mail.password}")
    private String password;  
    
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

        mailSender.setHost(mailHost);
        mailSender.setPort(Integer.valueOf(mailPort));
        mailSender.setUsername(userName);
        mailSender.setPassword(password);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", "true");

        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }
    
    
 // Use this code snippet in your app.
 // If you need more information about configurations or implementing the sample code, visit the AWS docs:
 // https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/java-dg-samples.html#prerequisites

 public static void getSecret() {

     String secretName = "dev/Appdata/letlock/postgres";
     String region = "us-west-2";

     // Create a Secrets Manager client
     AWSSecretsManager client  = AWSSecretsManagerClientBuilder.standard()
                                     .withRegion(region)
                                     .build();
     
     // In this sample we only handle the specific exceptions for the 'GetSecretValue' API.
     // See https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
     // We rethrow the exception by default.
     
     String secret, decodedBinarySecret;
     GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                     .withSecretId(secretName);
     GetSecretValueResult getSecretValueResult = null;

     try {
         getSecretValueResult = client.getSecretValue(getSecretValueRequest);
     } catch (DecryptionFailureException e) {
         // Secrets Manager can't decrypt the protected secret text using the provided KMS key.
         // Deal with the exception here, and/or rethrow at your discretion.
         throw e;
     } catch (InternalServiceErrorException e) {
         // An error occurred on the server side.
         // Deal with the exception here, and/or rethrow at your discretion.
         throw e;
     } catch (InvalidParameterException e) {
         // You provided an invalid value for a parameter.
         // Deal with the exception here, and/or rethrow at your discretion.
         throw e;
     } catch (InvalidRequestException e) {
         // You provided a parameter value that is not valid for the current state of the resource.
         // Deal with the exception here, and/or rethrow at your discretion.
         throw e;
     } catch (ResourceNotFoundException e) {
         // We can't find the resource that you asked for.
         // Deal with the exception here, and/or rethrow at your discretion.
         throw e;
     }

     // Decrypts secret using the associated KMS CMK.
     // Depending on whether the secret is a string or binary, one of these fields will be populated.
     if (getSecretValueResult.getSecretString() != null) {
         secret = getSecretValueResult.getSecretString();
     }
     else {
         decodedBinarySecret = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
     }

     // Your code goes here.
 }

}
