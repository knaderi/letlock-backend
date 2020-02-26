package com.landedexperts.letlock.filetransfer.backend.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Properties;

import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.acmpca.model.InvalidRequestException;
import com.amazonaws.services.acmpca.model.ResourceNotFoundException;
import com.amazonaws.services.applicationdiscovery.model.InvalidParameterException;
import com.amazonaws.services.datapipeline.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.google.gson.Gson;
import com.landedexperts.letlock.filetransfer.backend.controller.OrderController;

@Component
public class LetLockPGDataSource extends PGSimpleDataSource {
    private final Logger logger = LoggerFactory.getLogger(LetLockPGDataSource.class);
    
    public LetLockPGDataSource() {
        super();
    }

    private static final String LETLOCK_FILETRANSFER = "letlock_filetransfer";

    private static final long serialVersionUID = 4425173994723283055L;

    @Value("${spring.profiles.active}")
    private String env;

    private static String secretValueResult = null;

    public  String getSecretValueResult() {

        if (secretValueResult == null) {
            secretValueResult = getSecret();
        }
        return secretValueResult;
    }

    @Override
    public Connection getConnection()throws SQLException {
        if ("local".equals(env) || "local".contentEquals(System.getProperty("spring.profiles.active"))) {
            return super.getConnection();
        } else{
            PGSimpleDataSource dataSource = new PGSimpleDataSource();
            Properties dataSourceProperties = getJsonASPropererities(getSecretValueResult());

            dataSource
                    .setServerName(dataSourceProperties.getProperty("host"));
            dataSource.setDatabaseName(LETLOCK_FILETRANSFER);
            dataSource.setPortNumber(Integer.parseInt(dataSourceProperties.getProperty("port")));
            // Get these from AWS secret manager
            dataSource.setUser(dataSourceProperties.getProperty("username"));
            dataSource.setPassword(dataSourceProperties.getProperty("password"));

            try {
                return dataSource.getConnection();
            } catch (Exception e) {
                IllegalArgumentException e1 = new IllegalArgumentException(
                        "couldn't open database connection: " + e.getMessage());
                e1.initCause(e);
                throw e1;
            }
        }
    }

    public  String getSecret() {

        String secretName = "dev" + "/Appdata/letlock/postgres";
        String region = "us-west-2";

        // Create a Secrets Manager client
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
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
        }catch (Exception e) {
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

    private  Properties getJsonASPropererities(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Properties.class);
    }

}
