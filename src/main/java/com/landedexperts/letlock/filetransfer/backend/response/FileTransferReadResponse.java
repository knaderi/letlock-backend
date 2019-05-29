package com.landedexperts.letlock.filetransfer.backend.response;

import java.util.Date;

public class FileTransferReadResponse extends ErrorCodeMessageResponse {
	final private String senderLoginName;
	final private String senderWalletAddressUuid;
	final private String senderWalletAddress;
	final private String receiverLoginName;
	final private String receiverWalletAddressUuid;
	final private String receiverWalletAddress;
	final private String smartContractAddress;
	final private String funding1RecPubkeyStatus;
	final private String funding1RecPubkeyTransactionHash;
	final private String funding2SendDocinfoStatus;
	final private String funding2SendDocinfoTransactionHash;
	final private String funding3RecFinalStatus;
	final private String funding3RecFinalTransactionHash;
	final private boolean fileTransferIsActive;
	final private Date fileTransferCreate;
	final private Date fileTransferUpdate;

	public FileTransferReadResponse(
		final String senderLoginName,
		final String senderWalletAddressUuid,
		final String senderWalletAddress,
		final String receiverLoginName,
		final String receiverWalletAddressUuid,
		final String receiverWalletAddress,
		final String smartContractAddress,
		final String funding1RecPubkeyStatus,
		final String funding1RecPubkeyTransactionHash,
		final String funding2SendDocinfoStatus,
		final String funding2SendDocinfoTransactionHash,
		final String funding3RecFinalStatus,
		final String funding3RecFinalTransactionHash,
		final boolean fileTransferIsActive,
		final Date fileTransferCreate,
		final Date fileTransferUpdate,
		final String errorCode,
		final String errorMessage
	) {
		super(errorCode, errorMessage);
		this.senderLoginName = senderLoginName;
		this.senderWalletAddressUuid = senderWalletAddressUuid;
		this.senderWalletAddress = senderWalletAddress;
		this.receiverLoginName = receiverLoginName;
		this.receiverWalletAddressUuid = receiverWalletAddressUuid;
		this.receiverWalletAddress = receiverWalletAddress;
		this.smartContractAddress = smartContractAddress;
		this.funding1RecPubkeyStatus = funding1RecPubkeyStatus;
		this.funding1RecPubkeyTransactionHash = funding1RecPubkeyTransactionHash;
		this.funding2SendDocinfoStatus = funding2SendDocinfoStatus;
		this.funding2SendDocinfoTransactionHash = funding2SendDocinfoTransactionHash;
		this.funding3RecFinalStatus = funding3RecFinalStatus;
		this.funding3RecFinalTransactionHash = funding3RecFinalTransactionHash;
		this.fileTransferIsActive = fileTransferIsActive;
		this.fileTransferCreate = fileTransferCreate;
		this.fileTransferUpdate = fileTransferUpdate;
	}

	public String getSenderLoginName() {
		return senderLoginName;
	}

	public String getSenderWalletAddressUuid() {
		return senderWalletAddressUuid;
	}

	public String getSenderWalletAddress() {
		return senderWalletAddress;
	}

	public String getReceiverLoginName() {
		return receiverLoginName;
	}

	public String getReceiverWalletAddressUuid() {
		return receiverWalletAddressUuid;
	}

	public String getReceiverWalletAddress() {
		return receiverWalletAddress;
	}

	public String getSmartContractAddress() {
		return smartContractAddress;
	}

	public String getFunding1RecPubkeyStatus() {
		return funding1RecPubkeyStatus;
	}

	public String getFunding1RecPubkeyTransactionHash() {
		return funding1RecPubkeyTransactionHash;
	}

	public String getFunding2SendDocinfoStatus() {
		return funding2SendDocinfoStatus;
	}

	public String getFunding2SendDocinfoTransactionHash() {
		return funding2SendDocinfoTransactionHash;
	}

	public String getFunding3RecFinalStatus() {
		return funding3RecFinalStatus;
	}

	public String getFunding3RecFinalTransactionHash() {
		return funding3RecFinalTransactionHash;
	}

	public boolean isFileTransferIsActive() {
		return fileTransferIsActive;
	}

	public Date getFileTransferCreate() {
		return fileTransferCreate;
	}

	public Date getFileTransferUpdate() {
		return fileTransferUpdate;
	}

}
