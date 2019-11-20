package com.landedexperts.letlock.filetransfer.backend.database.vo;

import java.util.Date;

import com.landedexperts.letlock.filetransfer.backend.response.ErrorCodeMessageResponse;

public class FileTransferInfoVO extends ErrorCodeMessageResponse {
	FileTransferInfoRecord fileTransferInfoRecord = new FileTransferInfoRecord();

	public FileTransferInfoRecord getFileTransferInfoRecord() {
		return fileTransferInfoRecord;
	}

	public String getFileTransferUuid() {
		return fileTransferInfoRecord.getFileTransferUuid();
	}

	public void setFileTransferUuid(String fileTransferUuid) {
		fileTransferInfoRecord.setFileTransferUuid(fileTransferUuid);
	}
	
	public String getSenderLoginName() {
		return fileTransferInfoRecord.getSenderLoginName();
	}

	public void setSenderLoginName(String senderLoginName) {
		fileTransferInfoRecord.setSenderLoginName(senderLoginName);
	}
	
	public String getSenderWalletAddressUuid() {
		return fileTransferInfoRecord.getSenderWalletAddressUuid();
	}

	public void setSenderWalletAddressUuid(String senderWalletAddressUuid) {
		fileTransferInfoRecord.setSenderWalletAddressUuid(senderWalletAddressUuid);
	}

	public String getSenderWalletAddress() {
		return fileTransferInfoRecord.getSenderWalletAddress();
	}

	public void setSenderWalletAddress(String senderWalletAddress) {
		fileTransferInfoRecord.setSenderWalletAddress(senderWalletAddress);
	}

	public String getReceiverLoginName() {
		return fileTransferInfoRecord.getReceiverLoginName();
	}

	public void setReceiverLoginName(String receiverLoginName) {
		fileTransferInfoRecord.setReceiverLoginName(receiverLoginName);
	}

	public String getReceiverWalletAddressUuid() {
		return fileTransferInfoRecord.getReceiverWalletAddressUuid();
	}

	public void setReceiverWalletAddressUuid(String receiverWalletAddressUuid) {
		fileTransferInfoRecord.setReceiverWalletAddressUuid(receiverWalletAddressUuid);
	}

	public String getReceiverWalletAddress() {
		return fileTransferInfoRecord.getReceiverWalletAddress();
	}

	public void setReceiverWalletAddress(String receiverWalletAddress) {
		fileTransferInfoRecord.setReceiverWalletAddress(receiverWalletAddress);
	}

	public String getSmartContractAddress() {
		return fileTransferInfoRecord.getSmartContractAddress();
	}

	public void setSmartContractAddress(String smartContractAddress) {
		fileTransferInfoRecord.setSmartContractAddress(smartContractAddress);
	}

	public String getFunding1RecPubkeyStatus() {
		return fileTransferInfoRecord.getFunding1RecPubkeyStatus();
	}

	public void setFunding1RecPubkeyStatus(String funding1RecPubkeyStatus) {
		fileTransferInfoRecord.setFunding1RecPubkeyStatus(funding1RecPubkeyStatus);
	}

	public String getFunding1RecPubkeyTransactionHash() {
		return fileTransferInfoRecord.getFunding1RecPubkeyTransactionHash();
	}

	public void setFunding1RecPubkeyTransactionHash(String funding1RecPubkeyTransactionHash) {
		fileTransferInfoRecord.setFunding1RecPubkeyTransactionHash(funding1RecPubkeyTransactionHash);
	}

	public String getFunding2SendDocinfoStatus() {
		return fileTransferInfoRecord.getFunding2SendDocinfoStatus();
	}

	public void setFunding2SendDocinfoStatus(String funding2SendDocinfoStatus) {
		fileTransferInfoRecord.setFunding2SendDocinfoStatus(funding2SendDocinfoStatus);
	}

	public String getFunding2SendDocinfoTransactionHash() {
		return fileTransferInfoRecord.getFunding2SendDocinfoTransactionHash();
	}

	public void setFunding2SendDocinfoTransactionHash(String funding2SendDocinfoTransactionHash) {
		fileTransferInfoRecord.setFunding2SendDocinfoTransactionHash(funding2SendDocinfoTransactionHash);
	}

	public String getFunding3RecFinalStatus() {
		return fileTransferInfoRecord.getFunding3RecFinalStatus();
	}

	public void setFunding3RecFinalStatus(String funding3RecFinalStatus) {
		fileTransferInfoRecord.setFunding3RecFinalStatus(funding3RecFinalStatus);
	}

	public String getFunding3RecFinalTransactionHash() {
		return fileTransferInfoRecord.getFunding3RecFinalTransactionHash();
	}

	public void setFunding3RecFinalTransactionHash(String funding3RecFinalTransactionHash) {
		fileTransferInfoRecord.setFunding3RecFinalTransactionHash(funding3RecFinalTransactionHash);
	}

	public boolean isFileTransferIsActive() {
		return fileTransferInfoRecord.isFileTransferIsActive();
	}

	public void setFileTransferIsActive(boolean fileTransferIsActive) {
		fileTransferInfoRecord.setFileTransferIsActive(fileTransferIsActive);
	}

	public Date getFileTransferCreate() {
		return fileTransferInfoRecord.getFileTransferCreate();
	}

	public void setFileTransferCreate(Date fileTransferCreate) {
		fileTransferInfoRecord.setFileTransferCreate(fileTransferCreate);
	}

	public Date getFileTransferUpdate() {
		return fileTransferInfoRecord.getFileTransferUpdate();
	}

	public void setFileTransferUpdate(Date fileTransferUpdate) {
		fileTransferInfoRecord.setFileTransferUpdate(fileTransferUpdate);
	}
}
