/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Irina Soboleva - 2020
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ContactResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public interface ContactMapper {
    @Select(
            "SELECT"
                + " _return_code AS \"returnCode\","
                + " _return_message AS \"returnMessage\""
                + " FROM users.add_contact( #{ userId }, #{ contactUserName }, #{ contactLabel } )"
        )
    ReturnCodeMessageResponse createContact(
                @Param("userId") long userId,
                @Param("contactUserName") String contactUserName,
                @Param("contactLabel") String contactLabel
            );
    
    @Select(
            "SELECT "
                    + " _result AS result,"
                    + " _return_code AS \"returnCode\","
                    + " _return_message AS \"returnMessage\""
                + " FROM users.list_contacts( #{ userId } )"
        )
    ContactResponse getUserContacts(
                @Param("userId") long userId
            );

    @Select(
            "SELECT"
                + " _return_code AS \"returnCode\","
                + " _return_message AS \"returnMessage\""
                + " FROM users.update_contact( #{ userId }, #{ contactUserName }, #{ contactLabel }, #{ deleted } )"
        )
    ReturnCodeMessageResponse updateContact(
                @Param("userId") long userId,
                @Param("contactUserName") String contactUserName,
                @Param("contactLabel") String contactLabel,
                @Param("deleted") Boolean deleted
            );
    

}