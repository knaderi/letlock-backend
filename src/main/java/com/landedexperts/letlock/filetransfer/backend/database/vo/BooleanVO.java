package com.landedexperts.letlock.filetransfer.backend.database.vo;

import com.landedexperts.letlock.filetransfer.backend.response.ErrorCodeMessageResponse;

public class BooleanVO extends ErrorCodeMessageResponse {
	private boolean value;

	public boolean getValue() {
		return value;
	}

	public void setValue(final boolean value) {
		this.value = value;
	}
}
