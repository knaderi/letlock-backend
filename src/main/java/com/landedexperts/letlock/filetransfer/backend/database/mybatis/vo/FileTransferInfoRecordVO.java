/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import java.util.Date;

public class FileTransferInfoRecordVO {

    private String fileTransferUuid;
    private String senderLoginName;
    private String senderWalletAddressUuid;
    private String senderWalletAddress;
    private String receiverLoginName;
    private String receiverWalletAddressUuid;
    private String receiverWalletAddress;
    private String smartContractAddress;
    private String funding1RecPubkeyStatus;
    private String funding1RecPubkeyTransactionHash;
    private String funding2SendDocinfoStatus;
    private String funding2SendDocinfoTransactionHash;
    private String funding3RecFinalStatus;
    private String funding3RecFinalTransactionHash;
    private String fileTransferActiveCode;
    private String gateway;
    private String clearFileHash;
    private String encryptedFileHash;
    private String fileTransferCurrentStep;
    private String fileTransferCurrentStepStatus;
    private Date fileTransferCreate;
    private Date fileTransferUpdate;

    @Override
    public String toString() {
        return "FileTransferInfoRecordVO [fileTransferUuid="
                + fileTransferUuid
                + ", senderLoginName="
                + senderLoginName
                + ", senderWalletAddressUuid="
                + senderWalletAddressUuid
                + ", senderWalletAddress="
                + senderWalletAddress
                + ", receiverLoginName="
                + receiverLoginName
                + ", receiverWalletAddressUuid="
                + receiverWalletAddressUuid
                + ", receiverWalletAddress="
                + receiverWalletAddress
                + ", smartContractAddress="
                + smartContractAddress
                + ", funding1RecPubkeyStatus="
                + funding1RecPubkeyStatus
                + ", funding1RecPubkeyTransactionHash="
                + funding1RecPubkeyTransactionHash
                + ", funding2SendDocinfoStatus="
                + funding2SendDocinfoStatus
                + ", funding2SendDocinfoTransactionHash="
                + funding2SendDocinfoTransactionHash
                + ", funding3RecFinalStatus="
                + funding3RecFinalStatus
                + ", funding3RecFinalTransactionHash="
                + funding3RecFinalTransactionHash
                + ", fileTransferCurrentStep="
                + fileTransferCurrentStep
                + ", fileTransferCurrentStepStatus="
                + fileTransferCurrentStepStatus
                + ", fileTransferActiveCode="
                + fileTransferActiveCode
                + ", isfileTransferIsActive="
                + isFileTransferIsActive()
                + ", gateway="
                + gateway
                + ", clearFileHash="
                + clearFileHash
                + ", encryptedFileHash="
                + encryptedFileHash
                + ", fileTransferCreate="
                + fileTransferCreate
                + ", fileTransferUpdate="
                + fileTransferUpdate
                + "]";
    }

    public boolean isFileTransferIsActive() {
        boolean isActive = false;
        if (this.fileTransferActiveCode.equals("ACTIVE"))
            isActive = true;
        return isActive;

    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getClearFileHash() {
        return clearFileHash;
    }

    public void setClearFileHash(String clearFileHash) {
        this.clearFileHash = clearFileHash;
    }

    public String getEncryptedFileHash() {
        return encryptedFileHash;
    }

    public void setEncryptedFileHash(String encryptedFileHash) {
        this.encryptedFileHash = encryptedFileHash;
    }

    public String getFileTransferUuid() {
        return fileTransferUuid;
    }

    public void setFileTransferUuid(String fileTransferUuid) {
        this.fileTransferUuid = fileTransferUuid;
    }

    public String getSenderLoginName() {
        return senderLoginName;
    }

    public void setSenderLoginName(String senderLoginName) {
        this.senderLoginName = senderLoginName;
    }

    public String getSenderWalletAddressUuid() {
        return senderWalletAddressUuid;
    }

    public void setSenderWalletAddressUuid(String senderWalletAddressUuid) {
        this.senderWalletAddressUuid = senderWalletAddressUuid;
    }

    public String getSenderWalletAddress() {
        return senderWalletAddress;
    }

    public void setSenderWalletAddress(String senderWalletAddress) {
        this.senderWalletAddress = senderWalletAddress;
    }

    public String getReceiverLoginName() {
        return receiverLoginName;
    }

    public void setReceiverLoginName(String receiverLoginName) {
        this.receiverLoginName = receiverLoginName;
    }

    public String getReceiverWalletAddressUuid() {
        return receiverWalletAddressUuid;
    }

    public void setReceiverWalletAddressUuid(String receiverWalletAddressUuid) {
        this.receiverWalletAddressUuid = receiverWalletAddressUuid;
    }

    public String getReceiverWalletAddress() {
        return receiverWalletAddress;
    }

    public void setReceiverWalletAddress(String receiverWalletAddress) {
        this.receiverWalletAddress = receiverWalletAddress;
    }

    public String getSmartContractAddress() {
        return smartContractAddress;
    }

    public void setSmartContractAddress(String smartContractAddress) {
        this.smartContractAddress = smartContractAddress;
    }

    public String getFunding1RecPubkeyStatus() {
        return funding1RecPubkeyStatus;
    }

    public void setFunding1RecPubkeyStatus(String funding1RecPubkeyStatus) {
        this.funding1RecPubkeyStatus = funding1RecPubkeyStatus;
    }

    public String getFunding1RecPubkeyTransactionHash() {
        return funding1RecPubkeyTransactionHash;
    }

    public void setFunding1RecPubkeyTransactionHash(String funding1RecPubkeyTransactionHash) {
        this.funding1RecPubkeyTransactionHash = funding1RecPubkeyTransactionHash;
    }

    public String getFunding2SendDocinfoStatus() {
        return funding2SendDocinfoStatus;
    }

    public void setFunding2SendDocinfoStatus(String funding2SendDocinfoStatus) {
        this.funding2SendDocinfoStatus = funding2SendDocinfoStatus;
    }

    public String getFunding2SendDocinfoTransactionHash() {
        return funding2SendDocinfoTransactionHash;
    }

    public void setFunding2SendDocinfoTransactionHash(String funding2SendDocinfoTransactionHash) {
        this.funding2SendDocinfoTransactionHash = funding2SendDocinfoTransactionHash;
    }

    public String getFunding3RecFinalStatus() {
        return funding3RecFinalStatus;
    }

    public void setFunding3RecFinalStatus(String funding3RecFinalStatus) {
        this.funding3RecFinalStatus = funding3RecFinalStatus;
    }

    public String getFunding3RecFinalTransactionHash() {
        return funding3RecFinalTransactionHash;
    }

    public void setFunding3RecFinalTransactionHash(String funding3RecFinalTransactionHash) {
        this.funding3RecFinalTransactionHash = funding3RecFinalTransactionHash;
    }

    public String getFileTransferActiveCode() {
        return fileTransferActiveCode;
    }

    public void setFileTransferActiveCode(String fileTransferActiveCode) {
        this.fileTransferActiveCode = fileTransferActiveCode;
    }

    public Date getFileTransferCreate() {
        return fileTransferCreate;
    }

    public void setFileTransferCreate(Date fileTransferCreate) {
        this.fileTransferCreate = fileTransferCreate;
    }

    public Date getFileTransferUpdate() {
        return fileTransferUpdate;
    }

    public void setFileTransferUpdate(Date fileTransferUpdate) {
        this.fileTransferUpdate = fileTransferUpdate;
    }

    public String getFileTransferCurrentStep() {
        return fileTransferCurrentStep;
    }

    public void setFileTransferCurrentStep(String fileTransferCurrentStep) {
        this.fileTransferCurrentStep = fileTransferCurrentStep;
    }

    public String getFileTransferCurrentStepStatus() {
        return fileTransferCurrentStepStatus;
    }

    public void setFileTransferCurrentStepStatus(String fileTransferCurrentStepStatus) {
        this.fileTransferCurrentStepStatus = fileTransferCurrentStepStatus;
    }
}
