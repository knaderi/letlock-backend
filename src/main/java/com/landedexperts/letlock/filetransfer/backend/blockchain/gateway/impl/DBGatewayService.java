package com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayService;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.FileTransferMapper;
import com.landedexperts.letlock.filetransfer.backend.database.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.response.TransactionHashResponse;

public class DBGatewayService extends BlockChainGatewayService {

        @Autowired
    FileTransferMapper fileTransferMapper;
    
    
    public DBGatewayService() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getWalletAddressFromTransaction(String transaction) throws Exception {        
       
        //should be able to find it using the db. However in GoChain engine uses the call below:
        //web3.eth.accounts.recoverTransaction(transaction)
        return transaction;
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
    public boolean deploySmartContract(UUID fileTransferUuid, String senderWalletAddress, String receiverWalletAddress) throws Exception {
       //TODO: return a random mock smart contract address
//        "SELECT" +
//        " gochain.file_transfer_set_contract_address(" +
//        " $1," +
//        " DECODE( $2, 'hex' )" +
//        " )",
        BooleanVO returnValue = fileTransferMapper.fileTransferSetContractAddress(fileTransferUuid, senderWalletAddress + receiverWalletAddress);
        return returnValue.getValue();
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
