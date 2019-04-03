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
public class IsLoginNameAvailableController {

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/is_login_name_available",
		produces = {"application/JSON"}
	)
	public BooleanAnswer isLoginNameAvailable(
		@RequestParam( value="loginName", defaultValue="" ) String loginName
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
			rs = stmt.executeQuery("SELECT * FROM \"user\".is_login_name_available('" + loginName + "')");
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
