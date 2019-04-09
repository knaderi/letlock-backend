package com.landedexperts.letlock.filetransfer.backend.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
	public static Connection newConnection() throws Exception {
		NewConnection blip = new NewConnection();
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(
			blip.getAddress(),
			blip.getLogin(),
			blip.getPassword()
		);
	}
}
