package com.landedexperts.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
 

@Component
public class DatabaseProperties {
 
    @Value("${db.serverName}")
    private String serverName;
 
    public String getServerName() {
		return serverName;
	}

	@Value("${db.portNumber}")
    private String portNumber;
 
    public String getPortNumber() {
		return portNumber;
	}

	@Value("${db.databaseName}")
    private String databaseName;
    
    public String getDatabaseName() {
		return databaseName;
	}

	@Value("${db.user}")
    private String user;
 
    public String getUser() {
		return user;
	}

	@Value("${db.password}")
    private String password;

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "DatabaseProperties [serverName=" + serverName + ", portNumber=" + portNumber + ", databaseName="
				+ databaseName + ", user=" + user + ", password=" + password + "]";
	}
 
 
 
}
