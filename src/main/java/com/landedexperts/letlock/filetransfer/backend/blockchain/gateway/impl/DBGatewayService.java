package com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.impl;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayService;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.FileTransferMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.TransactionHashResponse;

@Service
public class DBGatewayService extends BlockChainGatewayService {

    @Autowired
    FileTransferMapper fileTransferMapper;
    private final Logger logger = LoggerFactory.getLogger(DBGatewayService.class);
    
    private String senderWalletAddress = createWalletAddress();
    private String receiverWalletAddress = createWalletAddress();

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
        returnValue.setReturnCode("SUCCESS");
        returnValue.setReturnMessage("");
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
        String smartContractAddres = receiverWalletAddress;
        ReturnCodeMessageResponse answer = fileTransferMapper.fileTransferSetContractAddress(fileTransferUuid, smartContractAddres);
        fileTransferMapper.fileTransferSetTransferStepCompleted(fileTransferUuid, receiverWalletAddress, "step_1_rec_pubkey", createTransactionHash());
       // fileTransferMapper.fileTransferSetTransferStepCompleted(fileTransferUuid, senderWalletAddress, "step_2_send_doc_info", "94E6d91C51b44cCAF97fE69dD122968eE8672173");
        // BooleanVO returnValue =
        // fileTransferMapper.fileTransferSetContractAddress(fileTransferUuid,
        // "BB9bc244D798123fDe783fCc1C72d3Bb8C189413");
        if (null == answer || answer.getReturnCode().equals("SUCCESS")) {
            return true;
        } else {
            logger.error(answer.getReturnMessage());
            throw new Exception(answer.getReturnMessage());

        }

    }

    @Override
    public String fund(UUID fileTransferUuid, String signedTransactionHex, String step) throws Exception {
        fileTransferMapper.fileTransferSetTransferStepCompleted(fileTransferUuid, getWalletAddressForStep(step), step, signedTransactionHex);
        return signedTransactionHex;
    }
    
    public  String createWalletAddress() {

        int number = 40;
        char[] charArr = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };// hex digits array

        Random rand = new Random();
        String result = "";
        for (int x = 0; x < number; x++) {
            int resInt = rand.nextInt(charArr.length);// random array element
            result += charArr[resInt];
        }
        return result;
    }
    
    public String createTransactionHash() {
        return createWalletAddress();
    }

//    client.query(
//            "SELECT gochain.get_undeployed_smart_contract()",

}
