package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.landedexperts.letlock.filetransfer.backend.answer.BooleanAnswer;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.FileMapper;
import com.landedexperts.letlock.filetransfer.backend.database.result.FileResult;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class FileController {
	/* Timeout in milliseconds, 24 hrs */
	private static long fileLifeSpan = 86400000L;

	@Autowired
	FileMapper fileMapper;

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/file_insert",
		produces = {"application/JSON"}
	)
	public BooleanAnswer fileInsert(
		@RequestParam( value="token" ) String token,
		@RequestParam( value="file_transfer_uuid" ) UUID fileTransferUuid,
		@RequestParam( value="file" ) MultipartFile file
	) throws Exception
	{
		Boolean result = false;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			// Get the path of the uploaded file
			String pathname = "";

			// Set the expiring date
			Date expires = new Date((new Date()).getTime() + FileController.fileLifeSpan);

			FileResult answer = fileMapper.fileInsert(userId, fileTransferUuid, pathname, expires);

			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();

			result = errorCode.equals("NO_ERROR");
		}

		return new BooleanAnswer(result, errorCode, errorMessage);
	}
}
