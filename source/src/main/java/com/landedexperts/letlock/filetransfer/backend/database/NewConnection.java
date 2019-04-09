package com.landedexperts.letlock.filetransfer.backend.database;

public class NewConnection {

	private String address = "jdbc:postgresql://localhost:5432/letlock_filetransfer";
	public String getAddress() { return this.address; }

	private String login = "letlock_backend";
	public String getLogin() { return this.login; }

	private String password = "Ai#~eq:*|G?|b%t[qJBh8f6[";
	public String getPassword() { return this.password; }

	public NewConnection() {}
}
