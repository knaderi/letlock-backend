package com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper;

import java.util.Date;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ErrorCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.BooleanPathnameVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

public interface FileMapper {
	@Select(
		"SELECT"
			+ " _file_id AS id,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM \"storage\".insert_file_upload_record( #{ userId }, #{ fileTransferUuid }, #{ pathname }, #{ expires } )"
	)
	IdVO insertFileUploadRecord(
			@Param("userId") int userId,
			@Param("fileTransferUuid") UUID fileTransferUuid,
			@Param("pathname") String pathname,
			@Param("expires") Date expires
		);

	@Select(
		"SELECT"
			+ " _result AS value,"
			+ " _pathname AS pathName,"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM \"storage\".is_allowed_to_download_file( #{ userId }, #{ fileTransferUuid } )"
	)
	BooleanPathnameVO isAllowedToDownloadFile(
			@Param("userId") int userId,
			@Param("fileTransferUuid") UUID fileTransferUuid
		);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM \"storage\".set_file_downloaded( #{ userId }, #{ fileTransferUuid } )"
	)
	ErrorCodeMessageResponse setFileDownloaded(
			@Param("userId") int userId,
			@Param("fileTransferUuid") UUID fileTransferUuid
		);

	@Select(
		"SELECT"
			+ " _error_code AS errorCode,"
			+ " _error_message AS errorMessage"
			+ " FROM \"storage\".delete_file( #{ userId }, #{ fileTransferUuid } )"
	)
	ErrorCodeMessageResponse deleteFile(
			@Param("userId") int userId,
			@Param("fileTransferUuid") UUID fileTransferUuid
		);
}
