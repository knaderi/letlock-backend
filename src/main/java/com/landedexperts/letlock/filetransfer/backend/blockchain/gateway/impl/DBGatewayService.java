package com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayService;
import com.landedexperts.letlock.filetransfer.backend.controller.FileTransferController;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.FileTransferMapper;
import com.landedexperts.letlock.filetransfer.backend.database.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.ErrorCodeMessageVO;
import com.landedexperts.letlock.filetransfer.backend.response.TransactionHashResponse;

@Service
public class DBGatewayService extends BlockChainGatewayService {

    @Autowired
    FileTransferMapper fileTransferMapper;
    private final Logger logger = LoggerFactory.getLogger(DBGatewayService.class);
    
    private String senderWalletAddress;
    private String receiverWalletAddress;
    private String transactionHash;

    public DBGatewayService() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getWalletAddressFromTransaction(String transaction) throws Exception {

        // should be able to find it using the db. However in GoChain engine uses the
        // call below:
        // web3.eth.accounts.recoverTransaction(transaction)
        return transaction;
    }

    @Override
    public TransactionHashResponse getTransactionStatus(String transactionHash) throws Exception {
        TransactionHashResponse returnValue = new TransactionHashResponse();
        returnValue.setStatus("completed");
        returnValue.setTransactionHash("12345678");
        returnValue.setErrorCode("NO_ERROR");
        returnValue.setErrorMessage("");
        return returnValue;
    }

    @Override
    public boolean deploySmartContract(UUID fileTransferUuid, String senderWalletAddress, String receiverWalletAddress) throws Exception {
        // TODO: return a random mock smart contract address
//        "SELECT" +
//        " gochain.file_transfer_set_contract_address(" +
//        " $1," +
//        " DECODE( $2, 'hex' )" +
//        " )",
        this.senderWalletAddress = senderWalletAddress;
        this.receiverWalletAddress = senderWalletAddress;
        String smartContractAddres = senderWalletAddress.substring(0, 2).equals("0x") ? senderWalletAddress.substring(2)
                : senderWalletAddress;
        ErrorCodeMessageVO answer = fileTransferMapper.fileTransferSetContractAddress(fileTransferUuid, smartContractAddres);
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

//        client.query(
//                "SELECT" +
//                " gochain.file_transfer_set_funding_step_pending(" +
//                " $1," +
//                " DECODE( $2, 'hex' )," +
//                " $3" +
//                " )",

//        client.query(
//                "SELECT" +
//                " gochain.file_transfer_set_funding_step_completed(" +
//                " $1," +
//                " DECODE( $2, 'hex' )," +
//                " $3," +
//                " DECODE($4, 'hex')" +
//                " )",
        return signedTransactionHex;
    }

//    client.query(
//            "SELECT gochain.get_undeployed_smart_contract()",

}
