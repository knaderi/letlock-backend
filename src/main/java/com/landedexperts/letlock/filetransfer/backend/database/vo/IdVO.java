package com.landedexperts.letlock.filetransfer.backend.database.vo;

public class IdVO extends ErrorCodeMessageVO {
	private int id;

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "IdVO [id=" + id + ", hashCode()=" + hashCode() + ", getId()=" + getId() + ", getErrorCode()="
				+ getErrorCode() + ", getErrorMessage()=" + getErrorMessage() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdVO other = (IdVO) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
