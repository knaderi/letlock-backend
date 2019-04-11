package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.result.ErrorCodeMessageResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.LoginResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.RegisterResult;

public interface UserMapper {
	@Select("SELECT _user_id AS userId, _error_code AS errorCode, _error_message AS errorMessage FROM \"user\".user_insert( #{loginName}, #{password} )")
	RegisterResult register(@Param("loginName") String loginName, @Param("password") String password);

	@Select("SELECT _user_id AS userId, _error_code AS errorCode, _error_message AS errorMessage FROM \"user\".login( #{loginName}, #{password} )")
	LoginResult login(@Param("loginName") String loginName, @Param("password") String password);

	@Select("SELECT * FROM \"user\".user_change_password( #{ loginName } , #{ oldPassword } , #{ newPassword } )")
	ErrorCodeMessageResult changePassword(@Param("loginName") String loginName, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);
}
