package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ErrorCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ForgotPasswordResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.AlgoVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

public interface UserMapper {
    @Select("SELECT" + " _result AS value," + " _error_code AS errorCode," + " _error_message AS errorMessage"
            + " FROM users.is_login_name_available( #{ loginName } )")
	BooleanVO isLoginNameAvailable(@Param("loginName") String loginName);

	@Select("SELECT" + " _user_id AS id," + " _error_code AS errorCode," + " _error_message AS errorMessage"
			+ " FROM users.add_user( #{loginName}, #{email}, #{password} )")
	IdVO register(@Param("loginName") String loginName, @Param("email") String email, @Param("password") String password);

	@Select("SELECT" + " _hashing_algo AS hashingAlgo," + " _encoding_algo AS encodingAlgo,"
			+ " _error_code AS errorCode," + " _error_message AS errorMessage"
			+ " FROM users.get_user_password_algo( #{loginName} )")
	AlgoVO getUserPasswordAlgo(@Param("loginName") String loginName);

	@Select("SELECT" + " _user_id AS id," + " _error_code AS errorCode," + " _error_message AS errorMessage"
			+ " FROM users.login( #{loginName}, #{password} )")
	IdVO login(@Param("loginName") String loginName, @Param("password") String password);

	@Select("SELECT" + " _error_code AS errorCode," + " _error_message AS errorMessage"
			+ " FROM users.update_user_password( #{ loginName } , #{ oldPassword } , #{ newPassword } )")
	ErrorCodeMessageResponse updateUserPassword(@Param("loginName") String loginName, @Param("oldPassword") String oldPassword,
			@Param("newPassword") String newPassword);

	@Select("SELECT" + " _error_code AS errorCode," + " _error_message AS errorMessage"
			+ " FROM users.update_user_status( #{userId} , #{ status } )")
	ErrorCodeMessageResponse updateUserStatus(@Param("userId") int userId, @Param("status") String status);
	
//	@Select("SELECT" + " _result AS value," + " _error_code AS errorCode," + " _error_message AS errorMessage"
//	            + " FROM users.is_email_registered( #{ email } )")
//	 BooleanVO isEmailRegistered(@Param("email") String email);
	
    @Select("SELECT" + " _result AS value," + " _error_code AS errorCode," + " _error_message AS errorMessage"
            + " FROM users.reset_user_password( #{email}, #{resetToken}, #{newPassword} )")
    BooleanVO resetUserPassword(@Param("email") String email, @Param("resetToken") String resetToken,  @Param("newPassword") String newPassword );
    
    @Select("SELECT" + " _result AS value," + " _error_code AS errorCode," + " _error_message AS errorMessage"
            + " FROM users.handle_forgot_password( #{email}, #{resetToken} )")
    BooleanVO handleForgotPassword(@Param("email") String email, @Param("resetToken") String resetToken );
    

    @Select("SELECT" +  " _error_code AS errorCode," + " _error_message AS errorMessage"
            + " FROM users.getLastRecord()")
    ErrorCodeMessageResponse getLastRecord();
    
    @Select("SELECT" + " _result AS value," +  " _error_code AS errorCode," + " _error_message AS errorMessage"
            + " FROM users.is_password_reset_token_valid( #{email}, #{resetToken})")
    BooleanVO isPasswordResetTokenValid(@Param("email") String email, @Param("resetToken") String resetToken);
}
