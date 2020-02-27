package com.landedexperts.letlock.filetransfer.backend.database;

import static com.landedexperts.letlock.filetransfer.backend.AWSSecretManagerFacade.DS_HOST_SECRET_KEY;
import static com.landedexperts.letlock.filetransfer.backend.AWSSecretManagerFacade.DS_PASSWORD_SECRET_KEY;
import static com.landedexperts.letlock.filetransfer.backend.AWSSecretManagerFacade.DS_PORT_SECRET_KEY;
import static com.landedexperts.letlock.filetransfer.backend.AWSSecretManagerFacade.DS_USER_SECRET_KEY;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.landedexperts.letlock.filetransfer.backend.AWSSecretManagerFacade;

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

    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";

    private static final String LOCAL_ENV_NAME = "local";

    public LetLockPGDataSource() {
        super();
    }
    
    private static final String LETLOCK_FILETRANSFER = "letlock_filetransfer";

    private static final long serialVersionUID = 4425173994723283055L;

    @Value("${spring.profiles.active}")
    private String env;

    @Value("${db.serverName}")
    private String localDBHost;

    @Value("${db.portNumber}")
    private int localDBPortNumber;

    @Value("${db.user}")
    private String localDBUserName;

    @Value("${db.password}")
    private String localDBPassword;

    private static String remoteDataSourceProperties = null;

    public String getRemoteDataSourceProperties() {

        if (remoteDataSourceProperties == null) {
            remoteDataSourceProperties = AWSSecretManagerFacade.getDataSourceProperties(env);
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
        try {
            if (isLocalEnv()) {
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

    private boolean isLocalEnv() {
        return LOCAL_ENV_NAME.equals(env) || LOCAL_ENV_NAME.contentEquals(System.getProperty(SPRING_PROFILES_ACTIVE));
    }

    /**
     * Initializes remote server datasource object using the datasource properties read from AWS secret manager
     * @return PGSimpleDataSource
     */
    private PGSimpleDataSource getRemoteEnvDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String remoteDataSourceProps = getRemoteDataSourceProperties();
        Properties dataSourceProperties = getJsonASProperties(remoteDataSourceProps);
        dataSource.setServerName(dataSourceProperties.getProperty(DS_HOST_SECRET_KEY));
        dataSource.setDatabaseName(LETLOCK_FILETRANSFER);
        dataSource.setPortNumber(Integer.parseInt(dataSourceProperties.getProperty(DS_PORT_SECRET_KEY)));
        // Get these from AWS secret manager
        dataSource.setUser(dataSourceProperties.getProperty(DS_USER_SECRET_KEY));
        dataSource.setPassword(dataSourceProperties.getProperty(DS_PASSWORD_SECRET_KEY));
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

    /**
     * This method creates a Java properties object using a json input
     * @param json
     * @return
     */
    private Properties getJsonASProperties(final String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Properties.class);
    }

}
