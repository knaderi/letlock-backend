package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.answer.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.answer.ConsumeResponse;
import com.landedexperts.letlock.filetransfer.backend.answer.FileTransferReadResponse;
import com.landedexperts.letlock.filetransfer.backend.answer.UuidResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.FileTransferMapper;
import com.landedexperts.letlock.filetransfer.backend.database.result.ConsumeVO;
import com.landedexperts.letlock.filetransfer.backend.database.result.FileTransferReadVO;
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
		value = "/file_transfer_read",
		produces = {"application/JSON"}
	)
	public FileTransferReadResponse fileTransferRead(
			@RequestParam( value="token" ) final String token,
			@RequestParam( value="file_transfer_uuid" ) final UUID fileTransferUuid
	) throws Exception
	{
		String senderLoginName = null;
		String senderWalletAddressUuid = null;
		String senderWalletAddress = null;
		String receiverLoginName = null;
		String receiverWalletAddressUuid = null;
		String receiverWalletAddress = null;
		String smartContractAddress = null;
		String funding1RecPubkeyStatus = null;
		String funding1RecPubkeyTransactionHash = null;
		String funding2SendDocinfoStatus = null;
		String funding2SendDocinfoTransactionHash = null;
		String funding3RecFinalStatus = null;
		String funding3RecFinalTransactionHash = null;
		Date fileTransferCreate = null;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			FileTransferReadVO answer = fileTransferMapper.fileTransferRead(userId, fileTransferUuid);

			senderLoginName = answer.getSenderLoginName();
			senderWalletAddressUuid = answer.getSenderWalletAddressUuid();
			senderWalletAddress = answer.getSenderWalletAddress();
			receiverLoginName = answer.getReceiverLoginName();
			receiverWalletAddressUuid = answer.getReceiverWalletAddressUuid();
			receiverWalletAddress = answer.getReceiverWalletAddress();
			smartContractAddress = answer.getSmartContractAddress();
			funding1RecPubkeyStatus = answer.getFunding1RecPubkeyStatus();
			funding1RecPubkeyTransactionHash = answer.getFunding1RecPubkeyTransactionHash();
			funding2SendDocinfoStatus = answer.getFunding2SendDocinfoStatus();
			funding2SendDocinfoTransactionHash = answer.getFunding2SendDocinfoTransactionHash();
			funding3RecFinalStatus = answer.getFunding3RecFinalStatus();
			funding3RecFinalTransactionHash = answer.getFunding3RecFinalTransactionHash();
			fileTransferCreate = answer.getFileTransferCreate();
			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
		}

		return new FileTransferReadResponse(
			senderLoginName,
			senderWalletAddressUuid,
			senderWalletAddress,
			receiverLoginName,
			receiverWalletAddressUuid,
			receiverWalletAddress,
			smartContractAddress,
			funding1RecPubkeyStatus,
			funding1RecPubkeyTransactionHash,
			funding2SendDocinfoStatus,
			funding2SendDocinfoTransactionHash,
			funding3RecFinalStatus,
			funding3RecFinalTransactionHash,
			fileTransferCreate,
			errorCode,
			errorMessage
		);
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
