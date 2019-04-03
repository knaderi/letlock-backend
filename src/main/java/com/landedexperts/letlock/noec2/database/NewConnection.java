package com.landedexperts.letlock.noec2.database;

public class NewConnection {

	private String address = "jdbc:postgresql://localhost:5432/letlock_filetransfer";
	public String getAddress() { return this.address; }

	private String login = "postgres";
	public String getLogin() { return this.login; }

	private String password = "julien";
	public String getPassword() { return this.password; }

	public NewConnection() {}
}
