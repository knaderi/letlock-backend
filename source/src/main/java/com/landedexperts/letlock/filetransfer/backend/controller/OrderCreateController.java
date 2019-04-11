package com.landedexperts.letlock.filetransfer.backend.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.answer.OrderAnswer;
import com.landedexperts.letlock.filetransfer.backend.database.ConnectionFactory;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class OrderCreateController {

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/order_create",
		produces = {"application/JSON"}
	)
	public OrderAnswer order_create(
			@RequestParam( value="token" ) String token
	) throws Exception
	{
		Integer orderId = -1;
		String errorMessage = "";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			Connection connection = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				connection = ConnectionFactory.newConnection();
				stmt = connection.createStatement();
				String sql = "SELECT * FROM payment.order_create(" + userId.toString() + ")";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					orderId = rs.getInt("_order_id");
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

		return new OrderAnswer(orderId, errorMessage);
	}
}
