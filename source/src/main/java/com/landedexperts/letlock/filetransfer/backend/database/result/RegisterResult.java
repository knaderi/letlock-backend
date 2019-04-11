package com.landedexperts.letlock.filetransfer.backend.database.result;

public class RegisterResult extends ErrorCodeMessageResult {
	private Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
