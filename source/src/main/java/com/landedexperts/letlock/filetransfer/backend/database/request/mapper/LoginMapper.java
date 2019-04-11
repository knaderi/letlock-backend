package com.landedexperts.letlock.filetransfer.backend.database.request.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.request.result.Login;

public interface LoginMapper {
	@Select("SELECT _user_id AS userId, _error_code AS errorCode, _error_message AS errorMessage FROM \"user\".login( #{loginName}, #{password} )")
	Login login(@Param("loginName") String loginName, @Param("password") String password);
}
