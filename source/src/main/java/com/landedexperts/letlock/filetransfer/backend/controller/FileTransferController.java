package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.answer.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.answer.ConsumeResponse;
import com.landedexperts.letlock.filetransfer.backend.answer.UuidResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.FileTransferMapper;
import com.landedexperts.letlock.filetransfer.backend.database.result.ConsumeVO;
import com.landedexperts.letlock.filetransfer.backend.database.result.GochainAddressVO;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class FileTransferController {
	@Autowired
	FileTransferMapper fileTransferMapper;

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/consume_start_file_transfer",
		produces = {"application/JSON"}
	)
	public ConsumeResponse consumeStartFileTransfer(
			@RequestParam( value="token" ) final String token,
			@RequestParam( value="wallet_address" ) final String walletAddress,
			@RequestParam( value="receiver_login_name" ) final String receiverLoginName
	) throws Exception
	{
		UUID fileTransferUuid = null;
		UUID walletAddressUuid = null;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			ConsumeVO answer = fileTransferMapper.consumeStartFileTransfer(userId, walletAddress, receiverLoginName);

			fileTransferUuid = answer.getFileTransferUuid();
			walletAddressUuid = answer.getWalletAddressUuid();
			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
		}

		return new ConsumeResponse(fileTransferUuid, walletAddressUuid, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/file_transfer_set_receiver_address",
		produces = {"application/JSON"}
	)
	public UuidResponse fileTransferSetReceiverAddress(
			@RequestParam( value="token" ) final String token,
			@RequestParam( value="file_transfer_uuid" ) final UUID fileTransferUuid,
			@RequestParam( value="wallet_address" ) final String walletAddress
	) throws Exception
	{
		UUID walletAddressUuid = null;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		int userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			GochainAddressVO answer = fileTransferMapper.setReceiverAddress(userId, fileTransferUuid, walletAddress);

			walletAddressUuid = answer.getGochainAddress();
			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
		}

		return new UuidResponse(walletAddressUuid, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/ask_funds",
		produces = {"application/JSON"}
	)
	public BooleanResponse askFunds(
			@RequestParam( value="file_transfer_uuid" ) final UUID fileTransferUuid,
			@RequestParam( value="signed_transaction_hex" ) final String signedTransactionHex,
			@RequestParam( value="step" ) final String step
	) throws Exception
	{
		Boolean result = false;



		return new BooleanResponse(result, "", "");
	}
}
