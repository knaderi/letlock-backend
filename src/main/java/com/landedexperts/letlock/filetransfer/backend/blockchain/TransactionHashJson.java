package com.landedexperts.letlock.filetransfer.backend.blockchain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TransactionHashJson {

    private String transactionHash;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

}
