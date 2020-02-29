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
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.BooleanPathnameVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

public interface FileMapper {
	@Select(
		"SELECT"
			+ " _file_id AS id,"
			+ " _return_code AS returnCode,"
			+ " _return_message AS returnMessage"
			+ " FROM \"storage\".insert_file_upload_record( #{ userId }, #{ fileTransferUuid }, #{ pathname }, #{ expires } )"
	)
	IdVO insertFileUploadRecord(
			@Param("userId") long userId,
			@Param("fileTransferUuid") UUID fileTransferUuid,
			@Param("pathname") String pathname,
			@Param("expires") Date expires
		);

	@Select(
		"SELECT"
			+ " _result AS value,"
			+ " _pathname AS pathName,"
			+ " _return_code AS returnCode,"
			+ " _return_message AS returnMessage"
			+ " FROM \"storage\".is_allowed_to_download_file( #{ userId }, #{ fileTransferUuid } )"
	)
	BooleanPathnameVO isAllowedToDownloadFile(
			@Param("userId") long userId,
			@Param("fileTransferUuid") UUID fileTransferUuid
		);

	@Select(
		"SELECT"
			+ " _return_code AS returnCode,"
			+ " _return_message AS returnMessage"
			+ " FROM \"storage\".set_file_downloaded( #{ userId }, #{ fileTransferUuid } )"
	)
	ReturnCodeMessageResponse setFileDownloaded(
			@Param("userId") long userId,
			@Param("fileTransferUuid") UUID fileTransferUuid
		);

	@Select(
		"SELECT"
			+ " _return_code AS returnCode,"
			+ " _return_message AS returnMessage"
			+ " FROM \"storage\".delete_file( #{ userId }, #{ fileTransferUuid } )"
	)
	ReturnCodeMessageResponse deleteFile(
			@Param("userId") long userId,
			@Param("fileTransferUuid") UUID fileTransferUuid
		);
}
