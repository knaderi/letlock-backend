package com.landedexperts.letlock.noec2.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.noec2.answer.AddressAnswer;
import com.landedexperts.letlock.noec2.database.ConnectionFactory;
import com.landedexperts.letlock.noec2.session.SessionManager;

@RestController
public class FileTransferSetReceiverAddressController {

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/file_transfer_set_receiver_address",
		produces = {"application/JSON"}
	)
	public AddressAnswer consumeStartOneFileTransfer(
			@RequestParam( value="token", defaultValue="" ) String token,
			@RequestParam( value="file_transfer_uuid", defaultValue="" ) String file_transfer_uuid,
			@RequestParam( value="wallet_address", defaultValue="" ) String wallet_address
	) throws Exception
	{
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
					"SELECT * FROM gochain.file_transfer_set_receiver_address("
						+ userId.toString() + ","
						+ "'" + file_transfer_uuid + "',"
						+ "DECODE('" + wallet_address + "', 'hex')"
						+ ")"
				);
				while(rs.next()) {
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

		return new AddressAnswer(walletAddressUuid, errorMessage);
	}
}
