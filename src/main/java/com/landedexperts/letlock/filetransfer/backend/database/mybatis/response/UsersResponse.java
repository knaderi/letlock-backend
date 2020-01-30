package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.UserVO;

public class UsersResponse extends ReturnCodeMessageResponse {

    public UsersResponse(final UserVO[] result, final String returnCode, final String returnMessage) {
        super(returnCode, returnMessage);
        this.result = result;
    }

    private final UserVO[] result;

    public UserVO[] getResult() {
        return result;
    }

}
