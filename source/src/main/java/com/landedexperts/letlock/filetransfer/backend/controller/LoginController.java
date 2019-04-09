package com.landedexperts.letlock.filetransfer.backend.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.answer.SessionTokenAnswer;
import com.landedexperts.letlock.filetransfer.backend.database.ConnectionFactory;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class LoginController {

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/login",
		produces = {"application/JSON"}
	)
	public SessionTokenAnswer login(
		@RequestParam( value="loginName" ) String loginName,
		@RequestParam( value="password" ) String password
	) throws Exception
	{
		Integer userId = -1;
		String error_message = "";

		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			connection = ConnectionFactory.newConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT * FROM \"user\".login('" + loginName + "','" + password + "')");
			while(rs.next()) {
				userId = rs.getInt("_user_id");
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

		String token = "";
		if(userId > 0) {
			token = SessionManager.generateSessionToken(userId);
		}

		return new SessionTokenAnswer(token, error_message);
	}
}
