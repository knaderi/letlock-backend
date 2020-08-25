/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.impl;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayService;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceTypeEnum;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.FileTransferMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.TransactionHashResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.IdVO;

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
        String senderAddressTrimmed = senderAddress.substring(0, 2).equals("0x") ? senderAddress.substring(2)
                : senderAddress;
        
        String receiverAddressTrimmed = receiverAddress.substring(0, 2).equals("0x") ? receiverAddress.substring(2)
                : receiverAddress;
        
        ReturnCodeMessageResponse answer = fileTransferMapper.fileTransferSetContractAddress(fileTransferUuid, createSmartContractAddress());
        if (null != answer && !answer.getReturnCode().equals("SUCCESS")) {
            logger.error(answer.getReturnMessage());
            throw new Exception(answer.getReturnMessage());
        }
        
        
        ReturnCodeMessageResponse response = fileTransferMapper.fileTransferSetTransferStepPending(fileTransferUuid, receiverAddressTrimmed, "step_1_rec_pubkey");
        if (null != response && !response.getReturnCode().equals("SUCCESS")) {
            logger.error(response.getReturnMessage());
            throw new Exception(response.getReturnMessage());
        }
        IdVO result = fileTransferMapper.setTransferFundingStepCompleted(fileTransferUuid, receiverAddressTrimmed, "step_1_rec_pubkey", createTransactionHash());
        if (null != result && !result.getReturnCode().equals("SUCCESS")) {
            logger.error(result.getReturnMessage());
            throw new Exception(result.getReturnMessage());
        }
        return true;
       // fileTransferMapper.fileTransferSetTransferStepCompleted(fileTransferUuid, senderWalletAddress, "step_2_send_doc_info", "94E6d91C51b44cCAF97fE69dD122968eE8672173");
        // BooleanResponse returnValue =
        // fileTransferMapper.fileTransferSetContractAddress(fileTransferUuid,
        // "BB9bc244D798123fDe783fCc1C72d3Bb8C189413");
      

    }

    @Override
    public String fund(UUID fileTransferUuid, String signedTransactionHex, String step) throws Exception {
        fileTransferMapper.setTransferFundingStepCompleted(fileTransferUuid, getWalletAddressForStep(step), step, signedTransactionHex);
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
        int number = 64;
        char[] charArr = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };// hex digits array

        Random rand = new Random();
        String result = "";
        for (int x = 0; x < number; x++) {
            int resInt = rand.nextInt(charArr.length);// random array element
            result += charArr[resInt];
        }
        return result;
    }
    
    public String createSmartContractAddress() {
        return createWalletAddress();
    }

    @Override
    public String getType() throws Exception {
        return BlockChainGatewayServiceTypeEnum.DB_GATEWAY.getValue();
    }

}
