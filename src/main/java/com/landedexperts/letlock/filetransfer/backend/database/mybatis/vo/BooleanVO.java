package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public class BooleanVO extends ReturnCodeMessageResponse {
	private boolean value;

	public boolean getValue() {
		return value;
	}

	public void setValue(final boolean value) {
		this.value = value;
	}
}
