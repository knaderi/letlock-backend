/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import java.util.Date;

public class LiteFileTransferRecordVO {

    private String fileTransferUuid;
    private String senderLoginName;
    private String receiverLoginName;
    private String fileTransferActiveCode;
    private String fileTransferCurrentStep;
    private String fileTransferCurrentStepStatus;
    private Date fileTransferCreate;
    private Date fileTransferUpdate;
    private String publicKey;
    private String clearFileHash;
    private String encryptedFileHash;
    private String encryptedFileKey;

    @Override
    public String toString() {
        return "LiteFileTransferRecordVO [fileTransferUuid="
                + fileTransferUuid
                + ", senderLoginName="
                + senderLoginName
                + ", receiverLoginName="
                + receiverLoginName
                + ", fileTransferCurrentStep="
                + fileTransferCurrentStep
                + ", fileTransferCurrentStepStatus="
                + fileTransferCurrentStepStatus
                + ", fileTransferActiveCode="
                + fileTransferActiveCode
                + ", isfileTransferIsActive="
                + isFileTransferIsActive()
                + ", fileTransferCreate="
                + fileTransferCreate
                + ", fileTransferUpdate="
                + fileTransferUpdate
                + ", publicKey="
                + publicKey
                + ", clearFileHash="
                + clearFileHash
                + ", encryptedFileHash="
                + encryptedFileHash
                + ", encryptedFileKey="
                + encryptedFileKey
                + "]";
    }

    public boolean isFileTransferIsActive() {
        boolean isActive = false;
            if (this.fileTransferActiveCode != null &&
                    (this.fileTransferActiveCode.equals("ACTIVE") || 
                            this.fileTransferActiveCode.equals("COMPLETED")))
                isActive = true;
        return isActive;
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

    public String getReceiverLoginName() {
        return receiverLoginName;
    }

    public void setReceiverLoginName(String receiverLoginName) {
        this.receiverLoginName = receiverLoginName;
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
    
    public String getPublicKey() {
        return this.publicKey;
    }
    
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    
    public String getClearFileHash() {
        return this.clearFileHash;
    }
    
    public void setClearFileHash(String clearFileHash) {
        this.clearFileHash = clearFileHash;
    }
    public String getEncryptedFileHash() {
        return this.encryptedFileHash;
    }
    
    public void setEncryptedFileHash(String encryptedFileHash) {
        this.encryptedFileHash = encryptedFileHash;
    }
    public String getEncryptedFileKey() {
        return this.encryptedFileKey;
    }
    
    public void setEncryptedFileKey(String encryptedFileKey) {
        this.encryptedFileKey = encryptedFileKey;
    }

}
