package com.landedexperts.letlock.filetransfer.backend;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.acmpca.model.InvalidRequestException;
import com.amazonaws.services.acmpca.model.ResourceNotFoundException;
import com.amazonaws.services.applicationdiscovery.model.InvalidParameterException;
import com.amazonaws.services.datapipeline.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;

public final class AWSSecretManagerFacade {
    
    
    private static final Logger logger = LoggerFactory.getLogger(AWSSecretManagerFacade.class);
    
    public static final String DS_PASSWORD_SECRET_KEY = "password";

    public static final String DS_USER_SECRET_KEY = "username";

    public static final String DS_PORT_SECRET_KEY = "port";

    public static final String DS_HOST_SECRET_KEY = "host";
    
    public static final String DEFAULT_AWS_CLOUD_REGION = "us-west-2";
        
    
    public static String getDataSourceProperties(String env) {

        String secretName = env + "/Appdata/letlock/postgres";

        // Create a Secrets Manager client
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(DEFAULT_AWS_CLOUD_REGION)
                .build();

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        } catch (DecryptionFailureException e) {
            // Secrets Manager can't decrypt the protected secret text using the provided
            // KMS key.
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
            return getSecretValueResult.getSecretString();
        } else {
            return new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
        }

    }

}
