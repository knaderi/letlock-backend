/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend;

import java.util.Base64;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.acmpca.model.InvalidRequestException;
import com.amazonaws.services.acmpca.model.ResourceNotFoundException;
import com.amazonaws.services.applicationdiscovery.model.InvalidParameterException;
import com.amazonaws.services.datapipeline.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.AWSSecretsManagerException;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.google.gson.Gson;

public final class AWSSecretManagerFacade {

    private static final Logger logger = LoggerFactory.getLogger(AWSSecretManagerFacade.class);
    
    private static final String APPDATA_LETLOCK_POSTGRES_SECRET = "/Appdata/letlock/postgres";
    
    private static final String APPDATA_LETLOCK_PAYPAL_SECRET = "/Appdata/letlock/paypal";
    
    private static final String APPDATA_LETLOCK_MAIL_SECRET = "/Appdata/letlock/mail";
    
    private static final String DEFAULT_AWS_CLOUD_REGION = "us-west-2";

    public static Properties getSpringMailProperties(String env) {

        String secretName = env + APPDATA_LETLOCK_MAIL_SECRET;
        return getSecretValue(secretName);

    }
    
    public static Properties getDataSourceProperties(String env) {

        String secretName = env + APPDATA_LETLOCK_POSTGRES_SECRET;
        return getSecretValue(secretName);

    }

    public static Properties getPayPalProperties(String mode) {

        String secretName = mode + APPDATA_LETLOCK_PAYPAL_SECRET;
        return getSecretValue(secretName);

    }
    
    private static Properties getSecretValue(String secretName) {
        Properties returnValue = new Properties();

        // Create a Secrets Manager client
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(DEFAULT_AWS_CLOUD_REGION)
                .build();

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;

        try {
            logger.info("trying to get teh secret value for secret request " + secretName);
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        }catch (DecryptionFailureException e) {
            // Secrets Manager can't decrypt the protected secret text using the provided
            // KMS key.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;

        } catch (AWSSecretsManagerException e) {
            throw e; 
        
        }  catch (InternalServiceErrorException e) {
            // An error occurred on the server side.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        } catch (InvalidParameterException e) {
            // You provided an invalid value for a parameter.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        } catch (InvalidRequestException e) {
            // You provided a parameter value that is not valid for the current state of the
            // resource.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        } catch (ResourceNotFoundException e) {
            // We can't find the resource that you asked for.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("very bad error occured", e.getMessage());
        }

        // Decrypts secret using the associated KMS CMK.
        // Depending on whether the secret is a string or binary, one of these fields
        // will be populated.
        if (getSecretValueResult.getSecretString() != null) {
            String properiesStr = getSecretValueResult.getSecretString();
            returnValue = getJsonASProperties(properiesStr);
            
        } else {
            logger.info("secret string is null, tryingto get the array");
            returnValue = getJsonASProperties(new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array()));
            logger.info("return value is " + returnValue);
        }
        return returnValue;
    }
    
    /**
     * This method creates a Java properties object using a json input
     * @param json
     * @return
     */
    private static Properties getJsonASProperties(final String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Properties.class);
    }

}
