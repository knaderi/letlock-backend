package com.landedexperts.letlock.filetransfer.backend.controller;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.landedexperts.letlock.filetransfer.backend.answer.BooleanAnswer;
import com.landedexperts.letlock.filetransfer.backend.database.ConnectionFactory;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class FileInsertController {

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/file_insert",
		produces = {"application/JSON"}
	)
	public BooleanAnswer fileInsert(
			@RequestParam( value="token" ) String token,
			@RequestParam( value="file_transfer_uuid" ) String file_transfer_uuid,
			@RequestParam( value="file" ) MultipartFile file
	) throws Exception
	{
		Integer fileId = -1;
		Boolean result = false;
		String errorMessage = "";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			Connection connection = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				String pathname = "";

				InputStream streamToPutIntoS3 = file.getInputStream();

				LocalDateTime expires = LocalDateTime.now().plusDays(1L);

				connection = ConnectionFactory.newConnection();
				stmt = connection.createStatement();
				rs = stmt.executeQuery(
					"SELECT * FROM \"storage\".file_insert("
						+ userId.toString() + ","
						+ "'" + file_transfer_uuid + "',"
						+ "'" + pathname + "',"
						+ "'" + expires.toString() + "'"
						+ ")"
				);
				while(rs.next()) {
					fileId = rs.getInt("_file_id");
					result = rs.getString("_error_code").equals("NO_ERROR");
					errorMessage = rs.getString("_error_message");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+e.getMessage());
				System.exit(0);
			} finally {
				if( rs != null ) { rs.close(); }
				if( stmt != null ) { stmt.close(); }
				if( connection != null ) { connection.close(); }
			}
		}

		return new BooleanAnswer(result, errorMessage);
	}
}
