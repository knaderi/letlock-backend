package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.UserVO;

public class UsersResponse extends ErrorCodeMessageResponse {

    public UsersResponse(final UserVO[] value, final String errorCode, final String errorMessage) {
        super(errorCode, errorMessage);
        this.value = value;
    }

    private final UserVO[] value;

    public UserVO[] getValue() {
        return value;
    }

}
