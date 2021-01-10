/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.AlgoVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.ResetTokenVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.UserVO;

public interface UserMapper {
    @Select("SELECT"
            + " _result AS value,"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM users.is_login_name_available( #{ loginId } )")
    BooleanResponse isLoginNameAvailable(@Param("loginId") String loginId);

    @Select("SELECT"
            + " _user_id AS id,"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM users.add_user( #{loginName}, #{email}, #{password}, #{resetToken})")
    IdVO register(@Param("loginName") String loginName, @Param("email") String email, @Param("password") String password,
            @Param("resetToken") String resetToken);

    @Select("SELECT"
            + " _user_id AS id,"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM users.add_user_via_partner( #{loginName}, #{email}, #{password}, #{resetToken}, #{redeemCode}, #{partnerName})")
    IdVO registerViaPartner(@Param("loginName") String loginName, @Param("email") String email, @Param("password") String password,
            @Param("resetToken") String resetToken, @Param("redeemCode") String redeemCode, @Param("partnerName") String partnerName);

    @Select("SELECT"
            + " * "
            + " FROM users.is_redeem_code_valid( #{redeemCode}, #{partnerName})")
    BooleanResponse isRedeemCodeValid(@Param("redeemCode") String redeemCode, @Param("partnerName") String partnerName);

    @Select("SELECT"
            + " _hashing_algo AS hashingAlgo,"
            + " _encoding_algo AS encodingAlgo,"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM users.get_user_password_algo( #{loginName} )")
    AlgoVO getUserPasswordAlgo(@Param("loginName") String loginName);

    @Select("SELECT"
            + " _user_id AS id,"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM users.login( #{loginId}, #{password} , #{requestData})")
    IdVO login(@Param("loginId") String loginId, @Param("password") String password, @Param("requestData") String requestData);

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM users.update_user_password( #{ loginName } , #{ oldPassword } , #{ newPassword }, #{requestData} )")
    ReturnCodeMessageResponse updateUserPassword(@Param("loginName") String loginName, @Param("oldPassword") String oldPassword,
            @Param("newPassword") String newPassword, @Param("requestData") String requestData);

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM users.update_user_status( #{userId} , #{ status } )")
    ReturnCodeMessageResponse updateUserStatus(@Param("userId") long userId, @Param("status") String status);

    @Select("SELECT"
            + " _result AS value,"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM users.reset_user_password( #{email}, #{resetToken}, #{newPassword} )")
    BooleanResponse resetUserPassword(@Param("email") String email, @Param("resetToken") String resetToken,
            @Param("newPassword") String newPassword);

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM users.handle_forgot_password( #{email}, #{resetToken} )")
    ReturnCodeMessageResponse handleForgotPassword(@Param("email") String email, @Param("resetToken") String resetToken);

    @Select("SELECT"
            + " _result AS value,"
            + " _return_code AS \"returnCode\","
            + " _return_message AS \"returnMessage\""
            + " FROM users.record_signup_confirmation( #{email}, #{resetToken} )")
    BooleanResponse confirmSignup(@Param("email") String email, @Param("resetToken") String resetToken);

    @Select("SELECT"
            + " _result AS value,"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM users.is_password_reset_token_valid( #{resetToken})")
    BooleanResponse isPasswordResetTokenValid(@Param("resetToken") String resetToken);

    @Select("SELECT _reset_token as resetToken, _user_id as userId  FROM  users.get_user_reset_token(#{email}, #{password})")
    ResetTokenVO getUserResetToken(@Param("email") String email, @Param("password") String password);

    @Select("SELECT _login_name as loginName, _email as email, _reset_token as resetToken, _status as status, _status_dt as statusDate, _create_dt as createdDate, _last_login as lastLoginDate, _update_dt as updatedDate  FROM  users.get_user_profile(#{userId})")
    UserVO getUserProfile(@Param("userId") long userId);

    @Update("UPDATE users.users"
            + " SET status = 'active',"
            + " reset_token = null "
            + " WHERE user_id = #{userId};")
    void setSignupConfirmationForTest(
            @Param("userId") long userId);

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage,"
            + "_order_id As id"
            + " FROM orders.add_free_credit("
            + " #{userId},"
            + "cast(#{customerLoginName} AS users.name)"
            + ")")
    IdVO addFreeTransferCredit(@Param("userId") long userId, @Param("customerLoginName") String customerLoginName);

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage,"
            + "_order_id As id"
            + " FROM orders.add_reedeem_code_credit("
            + " #{email},"
            + "redeem_code_id"
            + ")")
    IdVO addTransferCreditUsingRedeemCode(@Param("email") String email, @Param("redeem_code_id") String redeemCodeId);

    @Select("SELECT"
            + " * "
            + " FROM users.is_email_registered( #{email})")
    BooleanResponse isEmailRegistered(@Param("email") String email);

    @Select("SELECT"
            + " CAST( file_transfer_uuid AS text ) AS fileTransferUuid,"
            + " file_transfer_active_code AS fileTransferActiveCode"
            + " FROM gochain.get_file_transfer_sessions_for_user( #{ userId }  )"
            + " where file_transfer_active_code = 'ACTIVE'")
    Set<String> getActiveFileTransferUUIDs(@Param("userId") long userId);

}
