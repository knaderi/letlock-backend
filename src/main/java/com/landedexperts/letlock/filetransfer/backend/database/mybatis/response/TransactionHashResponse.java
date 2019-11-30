package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class TransactionHashResponse extends ErrorCodeMessageResponse {


    private String transactionHash;


    private String status;
    

//    private String errorCode;
//
//
//    private String errorMessage;

    public TransactionHashResponse(final String errorCode,
            final String errorMessage) {
        super(errorCode, errorMessage);
    }

    public TransactionHashResponse(final String transactionHash,
           final String errorCode, final String errorMessage) {
       // super(errorCode, errorMessage);
        this.transactionHash = transactionHash;
        status = "";
    }

    public TransactionHashResponse(final String transactionHash,
             final String status, String errorCode,
            final String errorMessage) {
     //   super(errorCode, errorMessage);
        this.transactionHash = transactionHash;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public TransactionHashResponse() {
        
    }
    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public String getStatus() {
        return status;
    }
}
