package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.blockchain.RestCall;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.FileTransferMapper;
import com.landedexperts.letlock.filetransfer.backend.database.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.ConsumeVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.ErrorCodeMessageVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.FileTransferReadVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.GochainAddressVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.UuidNameDateVO;
import com.landedexperts.letlock.filetransfer.backend.response.ConsumeResponse;
import com.landedexperts.letlock.filetransfer.backend.response.ErrorCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.response.FileTransferReadResponse;
import com.landedexperts.letlock.filetransfer.backend.response.UuidNameDateArrayResponse;
import com.landedexperts.letlock.filetransfer.backend.response.TransactionHashResponse;
import com.landedexperts.letlock.filetransfer.backend.response.UuidNameDate;
import com.landedexperts.letlock.filetransfer.backend.response.UuidResponse;
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
			String walletAddressTrimmed = walletAddress.substring(0, 2).equals("0x") ? walletAddress.substring(2) : walletAddress; 

			ConsumeVO answer = fileTransferMapper.consumeStartFileTransfer(userId, walletAddressTrimmed, receiverLoginName);

			fileTransferUuid = answer.getFileTransferUuid();
			walletAddressUuid = answer.getWalletAddressUuid();
			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
		}

		return new ConsumeResponse(fileTransferUuid, walletAddressUuid, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/file_transfer_waiting_receiver_address",
		produces = {"application/JSON"}
	)
	public UuidNameDateArrayResponse waitingForReceiverAddress(
		@RequestParam( value="token" ) final String token			
	) {
		UuidNameDate[] value = null;
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		Integer userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			UuidNameDateVO[] answer = fileTransferMapper.readSessionsWaitingForConfirmation(userId);

			value = new UuidNameDate[answer.length];
			for(int i = 0; i < answer.length; i++) {
				value[i] = new UuidNameDate(UUID.fromString(answer[i].getUuid()), answer[i].getName(), answer[i].getCreate());
			}
			errorCode = "NO_ERROR";
			errorMessage = "";
		}

		return new UuidNameDateArrayResponse(value, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/file_transfer_activate",
		produces = {"application/JSON"}
	)
	public ErrorCodeMessageResponse fileTransferActivate(
		@RequestParam( value="token" ) final String token,
		@RequestParam( value="file_transfer_uuid" ) final UUID fileTransferUuid
	) throws Exception
	{
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		int userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			ErrorCodeMessageVO answer = fileTransferMapper.fileTransferActivate(userId, fileTransferUuid);

			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
		}

		return new ErrorCodeMessageResponse(errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/file_transfer_deactivate",
		produces = {"application/JSON"}
	)
	public ErrorCodeMessageResponse fileTransferDeactivate(
		@RequestParam( value="token" ) final String token,
		@RequestParam( value="file_transfer_uuid" ) final UUID fileTransferUuid
	) throws Exception
	{
		String errorCode = "TOKEN_INVALID";
		String errorMessage = "Invalid token";

		int userId = SessionManager.getInstance().getUserId(token);
		if(userId > 0) {
			ErrorCodeMessageVO answer = fileTransferMapper.fileTransferDeactivate(userId, fileTransferUuid);

			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();
		}

		return new ErrorCodeMessageResponse(errorCode, errorMessage);
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
		boolean fileTransferIsActive = false;
		Date fileTransferCreate = null;
		Date fileTransferUpdate = null;
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
			fileTransferIsActive = answer.isFileTransferIsActive();
			fileTransferCreate = answer.getFileTransferCreate();
			fileTransferUpdate = answer.getFileTransferUpdate();
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
			fileTransferIsActive,
			fileTransferCreate,
			fileTransferUpdate,
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
			String walletAddressTrimmed = walletAddress.substring(0, 2).equals("0x") ? walletAddress.substring(2) : walletAddress;

			GochainAddressVO answer = fileTransferMapper.setReceiverAddress(userId, fileTransferUuid, walletAddressTrimmed);

			walletAddressUuid = answer.getGochainAddress();
			errorCode = answer.getErrorCode();
			errorMessage = answer.getErrorMessage();

			if(errorCode.equals("NO_ERROR")) {
				FileTransferReadVO fileTransferInfo = fileTransferMapper.fileTransferRead(userId, fileTransferUuid);
				String senderWalletAddress = fileTransferInfo.getSenderWalletAddress();
				String receiverWalletAddress = fileTransferInfo.getReceiverWalletAddress();

				@SuppressWarnings("unused")
				boolean response = RestCall.deploySmartContract(fileTransferUuid, "0x" + senderWalletAddress, "0x" + receiverWalletAddress);
			}
		}

		return new UuidResponse(walletAddressUuid, errorCode, errorMessage);
	}

	@RequestMapping(
		method = RequestMethod.POST,
		value = "/ask_funds",
		produces = {"application/JSON"}
	)
	public TransactionHashResponse askFunds(
		@RequestParam( value="file_transfer_uuid" ) final UUID fileTransferUuid,
		@RequestParam( value="signed_transaction_hex" ) final String signedTransactionHex,
		@RequestParam( value="step" ) final String step
	) throws Exception
	{
		String errorCode = "";
		String errorMessage = "";

		String walletAddress = RestCall.getWalletAddressFromTransaction(signedTransactionHex);

		String prefix = walletAddress.substring(0, 2);
		if(prefix.equals("0x")) {
			walletAddress = walletAddress.substring(2);
		}

		BooleanVO isAuthorized = fileTransferMapper.fileTransferIsStepAvailable(fileTransferUuid, walletAddress, step);

		errorCode = isAuthorized.getErrorCode();
		errorMessage = isAuthorized.getErrorMessage();

		String transactionHash = "";
		if(errorCode.equals("NO_ERROR") && isAuthorized.getValue()) {
			transactionHash = RestCall.fund(fileTransferUuid, signedTransactionHex, step);
		}

		return new TransactionHashResponse(transactionHash, errorCode, errorMessage);
	}
}