/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Irina Soboleva - 2020
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public interface TwoFAMapper {
    @Select(
            "SELECT"
                + " _return_code AS \"returnCode\","
                + " _return_message AS \"returnMessage\""
                + " FROM users.update_2fa_settings( #{ userId }, #{ password }, #{ email }, #{ enabled }, #{ phoneNumber }, #{requestData} )"
        )
    ReturnCodeMessageResponse update2FASettings(
                @Param("userId") long userId,
                @Param("password") String password,
                @Param("email") String email,
                @Param("enabled") Boolean enabled,
                @Param("phoneNumber") String phoneNumber,
                @Param("requestData") String requestData
            );
    
    @Select(
            "SELECT"
                + " _return_code AS \"returnCode\","
                + " _return_message AS \"returnMessage\""
                + " FROM users.verify_email( #{ userId }, #{ email } )"
        )
    ReturnCodeMessageResponse verifyEmail(
                @Param("userId") long userId,
                @Param("email") String email
            );
}
