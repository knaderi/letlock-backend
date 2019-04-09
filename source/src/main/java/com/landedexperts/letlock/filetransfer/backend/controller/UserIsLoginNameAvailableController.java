package com.landedexperts.letlock.filetransfer.backend.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.answer.BooleanAnswer;
import com.landedexperts.letlock.filetransfer.backend.database.ConnectionFactory;

@RestController
public class UserIsLoginNameAvailableController {

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/user_is_login_name_available",
		produces = {"application/JSON"}
	)
	public BooleanAnswer userIsLoginNameAvailable(
		@RequestParam( value="loginName" ) String loginName
	) throws Exception
	{
		Boolean result = false;
		String error_message = "";

		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			connection = ConnectionFactory.newConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT * FROM \"user\".user_is_login_name_available('" + loginName + "')");
			while(rs.next()) {
				result = rs.getBoolean("_result");
				error_message = rs.getString("_error_message");
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

		return new BooleanAnswer(result, error_message);
	}
}
