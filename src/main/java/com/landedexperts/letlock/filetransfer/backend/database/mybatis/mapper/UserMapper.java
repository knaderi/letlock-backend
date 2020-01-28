package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.AlgoVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.OrderInfoRecordVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.UserVO;

public interface UserMapper {
    @Select("SELECT" + " _result AS value," + " _return_code AS returnCode," + " _return_message AS returnMessage"
            + " FROM users.is_login_name_available( #{ loginName } )")
    BooleanVO isLoginNameAvailable(@Param("loginName") String loginName);

    @Select("SELECT" + " _user_id AS id," + " _return_code AS returnCode," + " _return_message AS returnMessage"
            + " FROM users.add_user( #{loginName}, #{email}, #{password} )")
    IdVO register(@Param("loginName") String loginName, @Param("email") String email, @Param("password") String password);

    @Select("SELECT" + " _hashing_algo AS hashingAlgo," + " _encoding_algo AS encodingAlgo,"
            + " _return_code AS returnCode," + " _return_message AS returnMessage"
            + " FROM users.get_user_password_algo( #{loginName} )")
    AlgoVO getUserPasswordAlgo(@Param("loginName") String loginName);

    @Select("SELECT" + " _user_id AS id," + " _return_code AS returnCode," + " _return_message AS returnMessage"
            + " FROM users.login( #{loginName}, #{password} )")
    IdVO login(@Param("loginName") String loginName, @Param("password") String password);

    @Select("SELECT" + " _return_code AS returnCode," + " _return_message AS returnMessage"
            + " FROM users.update_user_password( #{ loginName } , #{ oldPassword } , #{ newPassword } )")
    ReturnCodeMessageResponse updateUserPassword(@Param("loginName") String loginName, @Param("oldPassword") String oldPassword,
            @Param("newPassword") String newPassword);

    @Select("SELECT" + " _return_code AS returnCode," + " _return_message AS returnMessage"
            + " FROM users.update_user_status( #{userId} , #{ status } )")
    ReturnCodeMessageResponse updateUserStatus(@Param("userId") long userId, @Param("status") String status);

//	@Select("SELECT" + " _result AS value," + " _return_code AS returnCode," + " _return_message AS returnMessage"
//	            + " FROM users.is_email_registered( #{ email } )")
//	 BooleanVO isEmailRegistered(@Param("email") String email);

    @Select("SELECT" + " _result AS value," + " _return_code AS returnCode," + " _return_message AS returnMessage"
            + " FROM users.reset_user_password( #{email}, #{resetToken}, #{newPassword} )")
    BooleanVO resetUserPassword(@Param("email") String email, @Param("resetToken") String resetToken,
            @Param("newPassword") String newPassword);

    @Select("SELECT" + " _result AS value," + " _return_code AS returnCode," + " _return_message AS returnMessage"
            + " FROM users.handle_forgot_password( #{email}, #{resetToken} )")
    BooleanVO handleForgotPassword(@Param("email") String email, @Param("resetToken") String resetToken);

    @Select("SELECT" + " _result AS value," + " _return_code AS returnCode," + " _return_message AS returnMessage"
            + " FROM users.is_password_reset_token_valid( #{resetToken})")
    BooleanVO isPasswordResetTokenValid(@Param("resetToken") String resetToken);

    @Select("SELECT _reset_token as resetToken, _user_id as userId  FROM  users.get_user(#{email})")
    UserVO getUserObject(@Param("email") String email);
    
    @Select("SELECT *"
            + " FROM users.vw_user_orders"
            + " WHERE users.vw_user_orders.user_id = #{userId}")
    OrderInfoRecordVO[] getUserOrders(
            @Param("userId") long userId);
}
