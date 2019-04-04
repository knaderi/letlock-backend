package com.landedexperts.letlock.noec2.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.noec2.answer.OrderDetailAnswer;
import com.landedexperts.letlock.noec2.database.ConnectionFactory;
import com.landedexperts.letlock.noec2.session.SessionManager;

@RestController
public class OrderDetailAddController {

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/order_detail_add",
		produces = {"application/JSON"}
	)
	public OrderDetailAnswer orderDetailAdd(
		@RequestParam( value="token" ) String token,
		@RequestParam( value="order_id" ) String order_id,
		@RequestParam( value="product_id" ) String product_id,
		@RequestParam( value="quantity" ) String quantity
	) throws Exception
	{
		Integer orderDetailId = -1;
		String errorMessage = "";

		Integer userId = SessionManager.getUserId(token);
		if(userId > 0) {
			Connection connection = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				connection = ConnectionFactory.newConnection();
				stmt = connection.createStatement();
				String sql = "SELECT * FROM payment.order_detail_add("
						+ userId.toString() + ","
						+ order_id + ","
						+ product_id + ","
						+ quantity
						+ ")";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					orderDetailId = rs.getInt("_order_detail_id");
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

		return new OrderDetailAnswer(orderDetailId, errorMessage);
	}
}
