package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.UserVO;

public class UsersResponse extends ReturnCodeMessageResponse {

    public UsersResponse(final UserVO[] value, final String returnCode, final String returnMessage) {
        super(returnCode, returnMessage);
        this.value = value;
    }

    private final UserVO[] value;

    public UserVO[] getValue() {
        return value;
    }

}
