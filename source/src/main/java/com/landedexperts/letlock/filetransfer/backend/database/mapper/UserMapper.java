package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.answer.BooleanAnswer;
import com.landedexperts.letlock.filetransfer.backend.database.result.AlgoResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.ErrorCodeMessageResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.IdResult;

public interface UserMapper {
	@Select("SELECT"
			+ " _result AS value,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM \"user\".user_is_login_name_available( #{ loginName } )")
	BooleanAnswer isLoginNameAvailable(@Param("loginName") String loginName);

	@Select("SELECT"
			+ " _user_id AS id,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM \"user\".user_insert( #{loginName}, #{password} )")
	IdResult register(@Param("loginName") String loginName, @Param("password") String password);

	@Select("SELECT"
			+ " _hashing_algo AS hashingAlgo,"
			+ " _encoding_algo AS encodingAlgo,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM \"user\".user_get_password_algo( #{loginName} )")
	AlgoResult userGetPasswordAlgo(@Param("loginName") String loginName);

	@Select("SELECT"
			+ " _user_id AS id,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM \"user\".login( #{loginName}, #{password} )")
	IdResult login(@Param("loginName") String loginName, @Param("password") String password);

	@Select("SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM \"user\".user_change_password( #{ loginName } , #{ oldPassword } , #{ newPassword } )")
	ErrorCodeMessageResult changePassword(@Param("loginName") String loginName, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);

	@Select("SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM \"user\".user_change_status( #{ userId } , #{ status } )")
	ErrorCodeMessageResult changeStatus(@Param("userId") int userId, @Param("status") String status);
}