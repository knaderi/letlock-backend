package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class TransactionHashResponse extends ReturnCodeMessageResponse {


    private String transactionHash;


    private String status;
    

//    private String returnCode;
//
//
//    private String returnMessage;

    public TransactionHashResponse(final String returnCode,
            final String returnMessage) {
        super(returnCode, returnMessage);
    }

    public TransactionHashResponse(final String transactionHash,
           final String returnCode, final String returnMessage) {
       // super(returnCode, returnMessage);
        this.transactionHash = transactionHash;
        status = "";
    }

    public TransactionHashResponse(final String transactionHash,
             final String status, String returnCode,
            final String returnMessage) {
     //   super(returnCode, returnMessage);
        this.transactionHash = transactionHash;
        this.status = status;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
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
