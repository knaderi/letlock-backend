package com.landedexperts.letlock.filetransfer.backend.blockchain.gateway;

import java.util.UUID;

import com.landedexperts.letlock.filetransfer.backend.response.TransactionHashResponse;

public abstract class BlockChainGatewayService {

   

    /**
     * Retrieves wallet address using the transaction hash
     * @param fileTransferUuid TODO
     * @param step TODO
     * @param signedTransactionHex
     * 
     * @return String - Wallet address
     * @throws Exception
     */
    public abstract String getWalletAddressFromTransaction(UUID fileTransferUuid, String transaction, String step) throws Exception;

    /**
     * 
     * @param signedTransactionHex
     * @return
     * @throws Exception
     */
    public abstract TransactionHashResponse getTransactionStatus(String signedTransactionHex) throws Exception;

    /**
     * Deploys a smart contract containing the sender and receiver wallet addresses
     * and the file transfer UUID
     * 
     * @param fileTransferUuid
     * @param senderWalletAddress
     * @param receiverWalletAddress
     * @return boolean - true if deployed otherwise false
     * @throws Exception
     */
    public abstract boolean deploySmartContract(UUID fileTransferUuid, String senderWalletAddress, String receiverWalletAddress) throws Exception;

    /**
     * Call to fund the sender wallet using Main Go chain wallet. The wallet
     * addresses, gas needed, gas price and data are all stored in the
     * signedTransactionHex. This call internally in the Blockchain engine makes
     * call to the database to set the step after funding is done.
     * 
     * @param fileTransferUuid
     * @param signedTransactionHex - contains from and to wallet addresses,number of
     *                             gas needed, the gas price and the data being
     *                             transmitted.
     * @param step
     * @return
     * @throws Exception
     */
    public abstract String fund(UUID fileTransferUuid, String signedTransactionHex, String step) throws Exception;

}