package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public class IdVO extends ReturnCodeMessageResponse {
	private long id;

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "IdVO [id=" + id + ", hashCode()=" + hashCode() + ", getId()=" + getId() + ", getreturnCode()="
				+ getReturnCode() + ", getreturnMessage()=" + getReturnMessage() + ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}


	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
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
