package com.landedexperts.letlock.noec2.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.noec2.answer.BooleanAnswer;
import com.landedexperts.letlock.noec2.database.ConnectionFactory;

@RestController
public class RegisterController {

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/register",
		produces = {"application/JSON"}
	)
	public BooleanAnswer register(
		@RequestParam( value="loginName" ) String loginName,
		@RequestParam( value="password" ) String password
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
			rs = stmt.executeQuery("SELECT * FROM \"user\".user_insert('" + loginName + "','" + password + "')");
			while(rs.next()) {
				result = rs.getString("_error_code").equals("NO_ERROR");
				error_message = rs.getString("_error_message");
			}
			rs.close();
			stmt.close();
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
