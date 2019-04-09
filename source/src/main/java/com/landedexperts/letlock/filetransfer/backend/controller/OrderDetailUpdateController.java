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
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class OrderDetailUpdateController {

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/order_detail_update",
		produces = {"application/JSON"}
	)
	public BooleanAnswer orderDetailUpdate(
		@RequestParam( value="token" ) String token,
		@RequestParam( value="order_detail_id" ) String order_detail_id,
		@RequestParam( value="quantity" ) String quantity
	) throws Exception
	{
		Boolean result = false;
		String errorMessage = "";

		Integer userId = SessionManager.getUserId(token);
		if(userId > 0) {
			Connection connection = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				connection = ConnectionFactory.newConnection();
				stmt = connection.createStatement();
				String sql = "SELECT * FROM payment.order_detail_update("
						+ userId.toString() + ","
						+ order_detail_id + ","
						+ quantity
						+ ")";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					result = rs.getString("_error_code").equals("NO_ERROR");
					errorMessage = rs.getString("_error_message");
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
		}

		return new BooleanAnswer(result, errorMessage);
	}
}
