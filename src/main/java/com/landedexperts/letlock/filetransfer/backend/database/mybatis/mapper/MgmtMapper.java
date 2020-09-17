/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.JsonResponse;

public interface MgmtMapper {

    @Select("SELECT"
            + " _result AS value,"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM mgmt.is_free_signup_credit(cast (#{ appName } AS mgmt.tp_app_name))")
    JsonResponse<String> isFreeSignupTransferCredit(
            @Param("appName") String appName);

}
