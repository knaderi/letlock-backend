package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public class BooleanPathnameVO extends ReturnCodeMessageResponse {
	private boolean value;

	public boolean getValue() {
		return value;
	}

	public void setValue(final boolean value) {
		this.value = value;
	}

	private String pathName;

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
}
