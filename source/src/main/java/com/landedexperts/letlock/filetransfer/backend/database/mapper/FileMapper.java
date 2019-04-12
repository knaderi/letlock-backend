package com.landedexperts.letlock.filetransfer.backend.database.mapper;

import java.util.Date;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.landedexperts.letlock.filetransfer.backend.database.result.FileResult;

public interface FileMapper {
	@Select("SELECT"
			+ " _file_id AS fileId, _error_code AS errorCode, _error_message AS errorMessage"
			+ " FROM \"storage\".file_insert( #{ userId }, #{ fileTransferUuid }, #{ pathname }, #{ expires } )")
	FileResult fileInsert(
			@Param("userId") int userId,
			@Param("fileTransferUuid") UUID fileTransferUuid,
			@Param("pathname") String pathname,
			@Param("expires") Date expires
		);
}
