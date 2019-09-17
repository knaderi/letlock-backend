package com.landedexperts.letlock.filetransfer.backend.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Date;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.landedexperts.letlock.filetransfer.backend.database.mapper.FileMapper;
import com.landedexperts.letlock.filetransfer.backend.database.vo.BooleanPathnameVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.ErrorCodeMessageVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.IdVO;
import com.landedexperts.letlock.filetransfer.backend.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class FileController {
	/* Timeout in milliseconds, 24 hrs */
	private static long fileLifespan = 86400000L;

	@Autowired
	FileMapper fileMapper;

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/file_insert",
		produces = {"application/JSON"}
	)
	public BooleanResponse fileInsert(
		@RequestParam( value="token" ) final String token,
		@RequestParam( value="file_transfer_uuid" ) final UUID fileTransferUuid,
		@RequestParam( value="file" ) final MultipartFile file
	) throws Exception
	{
		Boolean result = false;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			// Get the path of the uploaded file
			String pathname = "C:\\Users\\Omer\\Documents\\projects\\letlock\\letlock-backend\\src\\test\\java\\local_files\\" + UUID.randomUUID().toString();

			// Set the expiring date
			Date expires = new Date((new Date()).getTime() + FileController.fileLifespan);

			IdVO answer = fileMapper.fileInsert(userId, fileTransferUuid, pathname, expires);

			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();

			result = errorCode.equals("NO_ERROR");

			InputStream fileStream = file.getInputStream();
			OutputStream localFileCopy = new FileOutputStream(pathname);
			try {
				IOUtils.copy(fileStream, localFileCopy);
			}
			finally {
				IOUtils.closeQuietly(fileStream);
				IOUtils.closeQuietly(localFileCopy);
			}
		}

		return new BooleanResponse(result, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/file_can_be_downloaded"
	)
	public BooleanResponse fileCanBeDownloaded(
		@RequestParam( value="token" ) final String token,
		@RequestParam( value="file_transfer_uuid" ) final UUID fileTransferUuid
	) throws Exception {
		boolean result = false;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			BooleanPathnameVO isAllowed = fileMapper.fileIsAllowedToDownload(userId, fileTransferUuid);

			result = isAllowed.getValue();
			errorCode = isAllowed.getErrorCode();
			errorMessage = isAllowed.getErrorMessage();
		}

		return new BooleanResponse(result, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/file_download"
	)
	public ResponseEntity<Resource> fileDownload(
		@RequestParam( value="token" ) final String token,
		@RequestParam( value="file_transfer_uuid" ) final UUID fileTransferUuid
	) throws Exception {
		@SuppressWarnings("unused")
		String errorCode = "TOKEN_INVALID";
		@SuppressWarnings("unused")
		String errorMessage = "Invalid token";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			BooleanPathnameVO isAllowed = fileMapper.fileIsAllowedToDownload(userId, fileTransferUuid);
			if(isAllowed.getValue()) {
				File file = new File(isAllowed.getPathName());
				FileInputStream fis = new FileInputStream(file);
				InputStreamResource isr = new InputStreamResource(fis);
				return ResponseEntity.ok()
						.contentLength(file.length())
						.contentType(MediaType.APPLICATION_OCTET_STREAM)
						.body(isr);
			}
		}

		/* Improve answer by responding with the error code and message */
		File empty = new File("C:\\Users\\Omer\\Documents\\projects\\letlock\\letlock-backend\\src\\test\\java\\local_files\\empty");
		FileInputStream fisEmpty = new FileInputStream(empty);
		InputStreamResource isrEmpty = new InputStreamResource(fisEmpty);
		return ResponseEntity.ok()
				.contentLength(empty.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(isrEmpty);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/file_delete",
		produces = {"application/JSON"}
	)
	public BooleanResponse fileDelete(
		@RequestParam( value="token" ) final String token,
		@RequestParam( value="file_transfer_uuid" ) final UUID fileTransferUuid
	) throws Exception
	{
		Boolean result = false;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			ErrorCodeMessageVO answer = fileMapper.fileDelete(userId, fileTransferUuid);

			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();

			result = errorCode.equals("NO_ERROR");
		}

		return new BooleanResponse(result, errorCode, errorMessage);
	}
}
