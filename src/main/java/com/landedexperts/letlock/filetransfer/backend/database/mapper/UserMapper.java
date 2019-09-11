package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.vo.AlgoVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.ErrorCodeMessageVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.IdVO;

public interface UserMapper {
	@Select("SELECT" + " _result AS value," + " _error_code AS errorCode," + " _error_message AS errorMessage"
			+ " FROM \"user\".is_login_name_available( #{ loginName } )")
	BooleanVO isLoginNameAvailable(@Param("loginName") String loginName);

	@Select("SELECT" + " _user_id AS id," + " _error_code AS errorCode," + " _error_message AS errorMessage"
			+ " FROM \"user\".add_user( #{loginName}, #{email}, #{password} )")
	IdVO register(@Param("loginName") String loginName, @Param("email") String email, @Param("password") String password);

	@Select("SELECT" + " _hashing_algo AS hashingAlgo," + " _encoding_algo AS encodingAlgo,"
			+ " _error_code AS errorCode," + " _error_message AS errorMessage"
			+ " FROM \"user\".get_user_password_algo( #{loginName} )")
	AlgoVO getUserPasswordAlgo(@Param("loginName") String loginName);

	@Select("SELECT" + " _user_id AS id," + " _error_code AS errorCode," + " _error_message AS errorMessage"
			+ " FROM \"user\".login( #{loginName}, #{password} )")
	IdVO login(@Param("loginName") String loginName, @Param("password") String password);

	@Select("SELECT" + " _error_code AS errorCode," + " _error_message AS errorMessage"
			+ " FROM \"user\".user_change_password( #{ loginName } , #{ oldPassword } , #{ newPassword } )")
	ErrorCodeMessageVO updateUserPassword(@Param("loginName") String loginName, @Param("oldPassword") String oldPassword,
			@Param("newPassword") String newPassword);

	@Select("SELECT" + " _error_code AS errorCode," + " _error_message AS errorMessage"
			+ " FROM \"user\".update_user_status( #{ userId } , #{ status } )")
	ErrorCodeMessageVO updateUserStatus(@Param("userId") int userId, @Param("status") String status);

}
