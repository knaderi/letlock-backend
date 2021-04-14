/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import java.util.Date;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

public class LiteFileTransferInfoVO extends ReturnCodeMessageResponse {
    LiteFileTransferRecordVO fileTransferRecord = new LiteFileTransferRecordVO();

    public LiteFileTransferRecordVO getFileTransferInfoRecord() {
        return fileTransferRecord;
    }

    public String getFileTransferUuid() {
        return fileTransferRecord.getFileTransferUuid();
    }

    public void setFileTransferUuid(String fileTransferUuid) {
        fileTransferRecord.setFileTransferUuid(fileTransferUuid);
    }

    public String getSenderLoginName() {
        return fileTransferRecord.getSenderLoginName();
    }

    public void setSenderLoginName(String senderName) {
        fileTransferRecord.setSenderLoginName(senderName);
    }

    public String getReceiverLoginName() {
        return fileTransferRecord.getReceiverLoginName();
    }

    public void setReceiverLoginName(String receiverLoginName) {
        fileTransferRecord.setReceiverLoginName(receiverLoginName);
    }

    public String getFileTransferActiveCode() {
        return fileTransferRecord.getFileTransferActiveCode();
    }

    public void setFileTransferActiveCode(String fileTransferActiveCode) {
        fileTransferRecord.setFileTransferActiveCode(fileTransferActiveCode);
    }

    public boolean isFileTransferIsActive() {
        return fileTransferRecord.isFileTransferIsActive();
    }

    public Date getFileTransferCreate() {
        return fileTransferRecord.getFileTransferCreate();
    }

    public void setFileTransferCreate(Date fileTransferCreate) {
        fileTransferRecord.setFileTransferCreate(fileTransferCreate);
    }

    public Date getFileTransferUpdate() {
        return fileTransferRecord.getFileTransferUpdate();
    }

    public void setFileTransferUpdate(Date fileTransferUpdate) {
        fileTransferRecord.setFileTransferUpdate(fileTransferUpdate);
    }
    
    public String getFileTransferCurrentStep() {
        return fileTransferRecord.getFileTransferCurrentStep();
    }

    public void setFileTransferCurrentStep(String fileTransferCurrentStep) {
        fileTransferRecord.setFileTransferCurrentStep(fileTransferCurrentStep);
    }

    public String getFileTransferCurrentStepStatus() {
        return fileTransferRecord.getFileTransferCurrentStepStatus();
    }

    public void setFileTransferCurrentStepStatus(String fileTransferCurrentStepStatus) {
        fileTransferRecord.setFileTransferCurrentStepStatus(fileTransferCurrentStepStatus);
    }
    
    public String getPublicKey() {
        return fileTransferRecord.getPublicKey();
    }
    
    public void setPublicKey(String publicKey) {
        fileTransferRecord.setPublicKey(publicKey);
    }
    
    public String getClearFileHash() {
        return fileTransferRecord.getClearFileHash();
    }
    
    public void setClearFileHash(String clearFileHash) {
        fileTransferRecord.setClearFileHash(clearFileHash);
    }
    public String getEncryptedFileHash() {
        return fileTransferRecord.getEncryptedFileHash();
    }
    
    public void setEncryptedFileHash(String encryptedFileHash) {
        fileTransferRecord.setEncryptedFileHash(encryptedFileHash);
    }
    public String getEncryptedFileKey() {
        return fileTransferRecord.getEncryptedFileKey();
    }
    
    public void setEncryptedFileKey(String encryptedFileKey) {
        fileTransferRecord.setEncryptedFileKey(encryptedFileKey);
    }
}
