package com.landedexperts.letlock.filetransfer.backend.database.result;

public class LoginResult extends ErrorCodeMessageResult {
	private int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
