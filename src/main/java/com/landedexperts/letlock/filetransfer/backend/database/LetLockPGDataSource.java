/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.stereotype.Component;

import com.landedexperts.letlock.filetransfer.backend.AWSSecretManagerFacade;
import com.landedexperts.letlock.filetransfer.backend.utils.LetLockBackendEnv;

/**
 * This class overwrite PGSimpleDataSource that is used by mybatis in beans.xml.
 * This class allows to initialized local datasource using the
 * application-local.properties and initialize the datasource for remote servers
 * (prod, qa, dev) using AWS secret manager
 * 
 * @author knaderi
 *
 */
@Component
public class LetLockPGDataSource extends PGSimpleDataSource {

    private static final String COULDN_T_OPEN_DATABASE_CONNECTION_ERROR = "couldn't open database connection: ";

    
    public LetLockPGDataSource() {
        super();
    }
    
    private static final String LETLOCK_FILETRANSFER = "letlock_filetransfer";

    private static final long serialVersionUID = 4425173994723283055L;


    private String localDBHost = "localhost";


    private int localDBPortNumber = 5432;


    private String localDBUserName = "letlock_backend";


    private String localDBPassword = "Ai#~eq:*|G?|b%t[qJBh8f6[";


    private static Properties remoteDataSourceProperties = null;

    public Properties getRemoteDataSourceProperties() {
        LetLockBackendEnv constants = LetLockBackendEnv.getInstance();
        if (remoteDataSourceProperties == null) {
            remoteDataSourceProperties = AWSSecretManagerFacade.getDataSourceProperties(constants.getEnv());
        }
        return remoteDataSourceProperties;
    }

    /**
     * This method creates and returns a connection for the specific environment the
     * application is deployed to
     */
    @Override
    public Connection getConnection() throws SQLException {
        PGSimpleDataSource dataSource = null;
        LetLockBackendEnv constants = LetLockBackendEnv.getInstance();
        try {
            if (constants.isLocalEnv()) {
                dataSource = getLocalEnvDataSource();

            } else {
                dataSource = getRemoteEnvDataSource();
            }
            return dataSource.getConnection();
        } catch (Exception e) {
            IllegalArgumentException e1 = new IllegalArgumentException(
                    COULDN_T_OPEN_DATABASE_CONNECTION_ERROR + e.getMessage());
            e1.initCause(e);
            throw e1;
        }
    }


    /**
     * Initializes remote server datasource object using the datasource properties read from AWS secret manager
     * @return PGSimpleDataSource
     */
    private PGSimpleDataSource getRemoteEnvDataSource() {
        LetLockBackendEnv constants = LetLockBackendEnv.getInstance();
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        Properties remoteDataSourceProps = getRemoteDataSourceProperties();
        dataSource.setServerName(remoteDataSourceProps.getProperty(constants.SECRET_DS_HOST_SECRET_KEY));
        dataSource.setDatabaseName(remoteDataSourceProps.getProperty(constants.SECRET_DS_DBNAME_SECRET_KEY));
        dataSource.setPortNumber(Integer.parseInt(remoteDataSourceProps.getProperty(constants.SECRET_DS_PORT_SECRET_KEY)));
        // Get these from AWS secret manager
        dataSource.setUser(remoteDataSourceProps.getProperty(constants.SECRET_DS_USER_SECRET_KEY));
        dataSource.setPassword(remoteDataSourceProps.getProperty(constants.SECRET_DS_PASSWORD_SECRET_KEY));
        return dataSource;
    }

    /**
     * Initializes local datasource object using application-local.properties
     * @return PGSimpleDataSource
     */
    private PGSimpleDataSource getLocalEnvDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName(localDBHost);
        dataSource.setDatabaseName(LETLOCK_FILETRANSFER);
        dataSource.setPortNumber(localDBPortNumber);
        dataSource.setUser(localDBUserName);
        dataSource.setPassword(localDBPassword);
        return dataSource;
    }

}
