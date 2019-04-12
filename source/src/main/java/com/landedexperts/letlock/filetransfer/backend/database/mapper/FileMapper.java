package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import java.util.Date;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.result.BooleanResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.ErrorCodeMessageResult;
import com.landedexperts.letlock.filetransfer.backend.database.result.IdResult;

public interface FileMapper {
	@Select(
		"SELECT"
			+ " _file_id AS id, _error_code AS errorCode, _error_message AS errorMessage"
			+ " FROM \"storage\".file_insert( #{ userId }, #{ fileTransferUuid }, #{ pathname }, #{ expires } )")
	IdResult fileInsert(
			@Param("userId") int userId,
			@Param("fileTransferUuid") UUID fileTransferUuid,
			@Param("pathname") String pathname,
			@Param("expires") Date expires
		);

	@Select(
		"SELECT"
			+ " _result AS value, _error_code AS errorCode, _error_message AS errorMessage"
			+ " FROM \"storage\".file_is_allowed_to_download( #{ userId }, #{ fileTransferUuid } )")
	BooleanResult fileIsAllowedToDownload(
			@Param("userId") int userId,
			@Param("fileTransferUuid") UUID fileTransferUuid
		);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode, _error_message AS errorMessage"
			+ " FROM \"storage\".file_set_downloaded( #{ userId }, #{ fileTransferUuid } )")
	ErrorCodeMessageResult fileSetDownloaded(
			@Param("userId") int userId,
			@Param("fileTransferUuid") UUID fileTransferUuid
		);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode, _error_message AS errorMessage"
			+ " FROM \"storage\".file_delete( #{ userId }, #{ fileTransferUuid } )")
	ErrorCodeMessageResult fileDelete(
			@Param("userId") int userId,
			@Param("fileTransferUuid") UUID fileTransferUuid
		);
}
