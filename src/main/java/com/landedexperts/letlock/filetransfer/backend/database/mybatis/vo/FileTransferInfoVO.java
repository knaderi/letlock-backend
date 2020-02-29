/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import java.util.Date;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public class FileTransferInfoVO extends ReturnCodeMessageResponse {
	FileTransferInfoRecordVO fileTransferInfoRecordVO = new FileTransferInfoRecordVO();

	public FileTransferInfoRecordVO getFileTransferInfoRecord() {
		return fileTransferInfoRecordVO;
	}

	public String getFileTransferUuid() {
		return fileTransferInfoRecordVO.getFileTransferUuid();
	}

	public void setFileTransferUuid(String fileTransferUuid) {
		fileTransferInfoRecordVO.setFileTransferUuid(fileTransferUuid);
	}
	
	public String getSenderLoginName() {
		return fileTransferInfoRecordVO.getSenderLoginName();
	}

	public void setSenderLoginName(String senderLoginName) {
		fileTransferInfoRecordVO.setSenderLoginName(senderLoginName);
	}
	
	public String getSenderWalletAddressUuid() {
		return fileTransferInfoRecordVO.getSenderWalletAddressUuid();
	}

	public void setSenderWalletAddressUuid(String senderWalletAddressUuid) {
		fileTransferInfoRecordVO.setSenderWalletAddressUuid(senderWalletAddressUuid);
	}

	public String getSenderWalletAddress() {
		return fileTransferInfoRecordVO.getSenderWalletAddress();
	}

	public void setSenderWalletAddress(String senderWalletAddress) {
		fileTransferInfoRecordVO.setSenderWalletAddress(senderWalletAddress);
	}

	public String getReceiverLoginName() {
		return fileTransferInfoRecordVO.getReceiverLoginName();
	}

	public void setReceiverLoginName(String receiverLoginName) {
		fileTransferInfoRecordVO.setReceiverLoginName(receiverLoginName);
	}

	public String getReceiverWalletAddressUuid() {
		return fileTransferInfoRecordVO.getReceiverWalletAddressUuid();
	}

	public void setReceiverWalletAddressUuid(String receiverWalletAddressUuid) {
		fileTransferInfoRecordVO.setReceiverWalletAddressUuid(receiverWalletAddressUuid);
	}

	public String getReceiverWalletAddress() {
		return fileTransferInfoRecordVO.getReceiverWalletAddress();
	}

	public void setReceiverWalletAddress(String receiverWalletAddress) {
		fileTransferInfoRecordVO.setReceiverWalletAddress(receiverWalletAddress);
	}

	public String getSmartContractAddress() {
		return fileTransferInfoRecordVO.getSmartContractAddress();
	}

	public void setSmartContractAddress(String smartContractAddress) {
		fileTransferInfoRecordVO.setSmartContractAddress(smartContractAddress);
	}

	public String getFunding1RecPubkeyStatus() {
		return fileTransferInfoRecordVO.getFunding1RecPubkeyStatus();
	}

	public void setFunding1RecPubkeyStatus(String funding1RecPubkeyStatus) {
		fileTransferInfoRecordVO.setFunding1RecPubkeyStatus(funding1RecPubkeyStatus);
	}

	public String getFunding1RecPubkeyTransactionHash() {
		return fileTransferInfoRecordVO.getFunding1RecPubkeyTransactionHash();
	}

	public void setFunding1RecPubkeyTransactionHash(String funding1RecPubkeyTransactionHash) {
		fileTransferInfoRecordVO.setFunding1RecPubkeyTransactionHash(funding1RecPubkeyTransactionHash);
	}

	public String getFunding2SendDocinfoStatus() {
		return fileTransferInfoRecordVO.getFunding2SendDocinfoStatus();
	}

	public void setFunding2SendDocinfoStatus(String funding2SendDocinfoStatus) {
		fileTransferInfoRecordVO.setFunding2SendDocinfoStatus(funding2SendDocinfoStatus);
	}

	public String getFunding2SendDocinfoTransactionHash() {
		return fileTransferInfoRecordVO.getFunding2SendDocinfoTransactionHash();
	}

	public void setFunding2SendDocinfoTransactionHash(String funding2SendDocinfoTransactionHash) {
		fileTransferInfoRecordVO.setFunding2SendDocinfoTransactionHash(funding2SendDocinfoTransactionHash);
	}

	public String getFunding3RecFinalStatus() {
		return fileTransferInfoRecordVO.getFunding3RecFinalStatus();
	}

	public void setFunding3RecFinalStatus(String funding3RecFinalStatus) {
		fileTransferInfoRecordVO.setFunding3RecFinalStatus(funding3RecFinalStatus);
	}

	public String getFunding3RecFinalTransactionHash() {
		return fileTransferInfoRecordVO.getFunding3RecFinalTransactionHash();
	}

	public void setFunding3RecFinalTransactionHash(String funding3RecFinalTransactionHash) {
		fileTransferInfoRecordVO.setFunding3RecFinalTransactionHash(funding3RecFinalTransactionHash);
	}

	public boolean isFileTransferIsActive() {
		return fileTransferInfoRecordVO.isFileTransferIsActive();
	}

	public void setFileTransferIsActive(boolean fileTransferIsActive) {
		fileTransferInfoRecordVO.setFileTransferIsActive(fileTransferIsActive);
	}

	public Date getFileTransferCreate() {
		return fileTransferInfoRecordVO.getFileTransferCreate();
	}

	public void setFileTransferCreate(Date fileTransferCreate) {
		fileTransferInfoRecordVO.setFileTransferCreate(fileTransferCreate);
	}

	public Date getFileTransferUpdate() {
		return fileTransferInfoRecordVO.getFileTransferUpdate();
	}

	public void setFileTransferUpdate(Date fileTransferUpdate) {
		fileTransferInfoRecordVO.setFileTransferUpdate(fileTransferUpdate);
	}
}
