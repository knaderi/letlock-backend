package com.landedexperts.letlock.filetransfer.backend.database.result;

public class BooleanResult extends ErrorCodeMessageResult {
	private boolean result;

	public boolean getResultId() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}
