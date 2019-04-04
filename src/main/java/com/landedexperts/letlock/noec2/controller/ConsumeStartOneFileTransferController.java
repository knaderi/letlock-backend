package com.landedexperts.letlock.noec2.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.noec2.answer.ConsumeAnswer;
import com.landedexperts.letlock.noec2.database.ConnectionFactory;
import com.landedexperts.letlock.noec2.session.SessionManager;

@RestController
public class ConsumeStartOneFileTransferController {

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/consume_start_one_file_transfer",
		produces = {"application/JSON"}
	)
	public ConsumeAnswer consumeStartOneFileTransfer(
			@RequestParam( value="token" ) String token,
			@RequestParam( value="wallet_address" ) String wallet_address,
			@RequestParam( value="receiver_login_name" ) String receiver_login_name
	) throws Exception
	{
		String fileTransferUuid = "";
		String walletAddressUuid = "";
		String errorMessage = "";

		Integer userId = SessionManager.getUserId(token);
		if(userId > 0) {
			Connection connection = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				connection = ConnectionFactory.newConnection();
				stmt = connection.createStatement();
				rs = stmt.executeQuery(
					"SELECT * FROM \"user\".consume_start_one_file_transfer("
						+ userId.toString() + ","
						+ "DECODE('" + wallet_address + "', 'hex'),"
						+ "'" + receiver_login_name + "'"
						+ ")"
				);
				while(rs.next()) {
					fileTransferUuid = rs.getString("_file_transfer_uuid");
					walletAddressUuid = rs.getString("_wallet_address_uuid");
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

		return new ConsumeAnswer(fileTransferUuid, walletAddressUuid, errorMessage);
	}
}
