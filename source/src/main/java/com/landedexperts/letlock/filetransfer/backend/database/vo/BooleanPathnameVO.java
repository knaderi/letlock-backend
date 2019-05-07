package com.landedexperts.letlock.filetransfer.backend.database.vo;

public class BooleanPathnameVO extends ErrorCodeMessageVO {
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
