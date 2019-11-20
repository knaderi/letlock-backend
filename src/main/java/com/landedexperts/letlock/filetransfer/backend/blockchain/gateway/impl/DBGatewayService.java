package com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayService;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.FileTransferMapper;
import com.landedexperts.letlock.filetransfer.backend.response.ErrorCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.response.TransactionHashResponse;

@Service
public class DBGatewayService extends BlockChainGatewayService {

    @Autowired
    FileTransferMapper fileTransferMapper;
    private final Logger logger = LoggerFactory.getLogger(DBGatewayService.class);
    
    private String senderWalletAddress;
    private String receiverWalletAddress;

    public DBGatewayService() {
        super();
    }

    @Override
    public String getWalletAddressFromTransaction(UUID fileTransferUuid, String transaction, String step) throws Exception {
        return getWalletAddressForStep(step);

        // should be able to find it using the db. However in GoChain engine uses the
        // call below:
        // web3.eth.accounts.recoverTransaction(transaction)
//        return transaction;
    }

    private String getWalletAddressForStep(String step) {
        if(step.equals("step_2_send_doc_info")) {
            return senderWalletAddress;
        }
        return receiverWalletAddress;
    }

    @Override
    public TransactionHashResponse getTransactionStatus(String transactionHash) throws Exception {
        TransactionHashResponse returnValue = new TransactionHashResponse();
        returnValue.setStatus("completed");
        returnValue.setTransactionHash(transactionHash);
        returnValue.setErrorCode("NO_ERROR");
        returnValue.setErrorMessage("");
        return returnValue;
    }

    @Override
    public boolean deploySmartContract(UUID fileTransferUuid, String senderAddress, String receiverAddress) throws Exception {

        // TODO: return a random mock smart contract address
//        "SELECT" +
//        " gochain.file_transfer_set_contract_address(" +
//        " $1," +
//        " DECODE( $2, 'hex' )" +
//        " )",
        this.senderWalletAddress = senderAddress.substring(0, 2).equals("0x") ? senderAddress.substring(2)
                : senderAddress;
        this.receiverWalletAddress = receiverAddress.substring(0, 2).equals("0x") ? receiverAddress.substring(2)
                : receiverAddress;
        String smartContractAddres = receiverWalletAddress;
        ErrorCodeMessageResponse answer = fileTransferMapper.fileTransferSetContractAddress(fileTransferUuid, smartContractAddres);
        fileTransferMapper.fileTransferSetTransferStepCompleted(fileTransferUuid, receiverWalletAddress, "step_1_rec_pubkey", "94E6d91C51b44cCAF97fE69dD122968eE8672173");
       // fileTransferMapper.fileTransferSetTransferStepCompleted(fileTransferUuid, senderWalletAddress, "step_2_send_doc_info", "94E6d91C51b44cCAF97fE69dD122968eE8672173");
        // BooleanVO returnValue =
        // fileTransferMapper.fileTransferSetContractAddress(fileTransferUuid,
        // "BB9bc244D798123fDe783fCc1C72d3Bb8C189413");
        if (null == answer || answer.getErrorCode().equals("NO_ERROR")) {
            return true;
        } else {
            logger.error(answer.getErrorMessage());
            throw new Exception(answer.getErrorMessage());

        }

    }

    @Override
    public String fund(UUID fileTransferUuid, String signedTransactionHex, String step) throws Exception {
        fileTransferMapper.fileTransferSetTransferStepCompleted(fileTransferUuid, getWalletAddressForStep(step), step, signedTransactionHex);
        return signedTransactionHex + "zzz";
    }

//    client.query(
//            "SELECT gochain.get_undeployed_smart_contract()",

}
