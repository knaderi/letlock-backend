/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import java.util.Date;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.UuidResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.BooleanPathnameVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.LiteFileTransferInfoVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.LiteFileTransferRecordVO;

public interface LiteFileTransferMapper {
    @Select("SELECT"
            + " _file_transfer_uuid AS \"fileTransferUuid\","
            + " _return_code AS \"returnCode\","
            + " _return_message AS \"returnMessage\" "
            + " FROM ft_lite.new_file_transfer_session( #{ userId }, #{ receiverLoginName })")
    UuidResponse createFileTransferSession(
            @Param("userId") long userId,
            @Param("receiverLoginName") String receiverLoginName);

    @Select("SELECT"
            + " #{ fileTransferUuid }::text AS \"fileTransferUuid\","
            + " _sender_login_name AS \"senderLoginName\","
            + " _receiver_login_name AS \"receiverLoginName\","
            + " _current_step AS \"fileTransferCurrentStep\","
            + " _current_step_status AS \"fileTransferCurrentStepStatus\","
            + " _active_code AS \"fileTransferActiveCode\","
            + " _create_dt AS \"fileTransferCreate\","
            + " _update_dt AS \"fileTransferUpdate\","
            + " _return_code AS \"returnCode\","
            + " _return_message AS \"returnMessage\" "
            + " FROM ft_lite.get_file_transfer_info( #{ userId }, #{ fileTransferUuid } )")
    LiteFileTransferInfoVO getFileTransferInfo(
            @Param("userId") long userId,
            @Param("fileTransferUuid") UUID fileTransferUuid);

    @Select("SELECT"
            + " file_transfer_uuid::text AS \"fileTransferUuid\","
            + " sender_login_name AS \"senderLoginName\","
            + " receiver_login_name AS \"receiverLoginName\","
            + " current_step AS \"fileTransferCurrentStep\","
            + " current_step_status AS \"fileTransferCurrentStepStatus\","
            + " active_code AS \"fileTransferActiveCode\","
            + " create_dt AS \"fileTransferCreate\","
            + " update_dt AS \"fileTransferUpdate\""
            + " FROM ft_lite.get_file_transfer_sessions_for_user( #{ userId } )")
    LiteFileTransferRecordVO[] getFileTransferSessionsForUser(
            @Param("userId") long userId);
    
    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM ft_lite.set_file_transfer_step("
            + " #{ fileTransferUuid },"
            + " #{ transferStep }::ft_lite.tp_current_step,"
            + " #{ transferStepStatus }::ft_lite.tp_current_step_status"
            + " )")
    ReturnCodeMessageResponse setTransferStep(
            @Param("fileTransferUuid") UUID fileTransferUuid,
            @Param("transferStep") String transferStep,
            @Param("transferStepStatus") String transferStepStatus);

    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM ft_lite.set_file_transfer_inactive( #{ userId }, #{ fileTransferUuid } )")
    ReturnCodeMessageResponse setFileTransferInactive(
            @Param("userId") long userId,
            @Param("fileTransferUuid") UUID fileTransferUuid);


    @Select("SELECT"
            + " _return_code AS returnCode,"
            + " _return_message AS returnMessage"
            + " FROM ft_lite.set_file_transfer_receiver_accept( #{ userId }, #{ fileTransferUuid }, #{ publicKey } )")
    ReturnCodeMessageResponse setFileTransferReceiverAccept(
            @Param("userId") long userId,
            @Param("fileTransferUuid") UUID fileTransferUuid,
            @Param("publicKey") String publicKey);

    @Select("SELECT"
            + " _public_key AS \"publicKey\","
            + " _return_code AS \"returnCode\","
            + " _return_message AS \"returnMessage\" "
            + " FROM ft_lite.get_file_transfer_public_key( #{ userId }, #{ fileTransferUuid } )")
    LiteFileTransferInfoVO getFileTransferPublicKey(
            @Param("userId") long userId,
            @Param("fileTransferUuid") UUID fileTransferUuid);
    
    @Select("SELECT"
            + " _clear_file_hash AS \"clearFileHash\","
            + " _encrypted_file_hash AS \"encryptedFileHash\","
            + " _encrypted_file_key AS \"encryptedFileKey\","
            + " _return_code AS \"returnCode\","
            + " _return_message AS \"returnMessage\" "
            + " FROM ft_lite.get_file_transfer_document_info( #{ userId }, #{ fileTransferUuid } )")
    LiteFileTransferInfoVO getFileTransferDocumentInfo(
            @Param("userId") long userId,
            @Param("fileTransferUuid") UUID fileTransferUuid);
    
    @Select("SELECT"
            + " _return_code AS \"returnCode\","
            + " _return_message AS \"returnMessage\" "
            + " FROM ft_lite.set_file_transfer_document_info( "
            + "  #{ userId }, #{ fileTransferUuid }, #{ documentHash }, #{ encryptedDocumentHash }, #{ encryptedDocumentKey } )")
    ReturnCodeMessageResponse setFileTransferDocumentInfo(
            @Param("userId") long userId,
            @Param("fileTransferUuid") UUID fileTransferUuid,
            @Param("documentHash") String documentHash,
            @Param("encryptedDocumentHash") String encryptedDocumentHash,
            @Param("encryptedDocumentKey") String encryptedDocumentKey);

    @Select("SELECT"
            + " _return_code AS \"returnCode\","
            + " _return_message AS \"returnMessage\" "
            + " FROM ft_lite.new_file_upload_record( "
            + "  #{ userId }, #{ fileTransferUuid }, #{ remotePathName }, #{ expires } )")
    ReturnCodeMessageResponse insertFileUploadRecord(
            @Param("userId") long userId,
            @Param("fileTransferUuid") UUID fileTransferUuid,
            @Param("remotePathName") String remotePathName,
            @Param("expires") Date expires);
    
    @Select(
            "SELECT"
                + " _result AS value,"
                + " _pathname AS \"pathName\","
                + " _return_code AS \"returnCode\","
                + " _return_message AS \"returnMessage\" "
                + " FROM ft_lite.get_file_path( #{ userId }, #{ fileTransferUuid } )")
        BooleanPathnameVO canDownloadFile(
                @Param("userId") long userId,
                @Param("fileTransferUuid") UUID fileTransferUuid);
    
}
