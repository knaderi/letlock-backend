package com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.impl;

import java.util.UUID;

import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayService;

public class DBGatewayService extends BlockChainGatewayService {

    public DBGatewayService() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getWalletAddressFromTransaction(String transaction) throws Exception {        
        return null;
    }

    @Override
    public String searchTransactionHash(String signedTransactionHex) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean deploySmartContract(UUID fileTransferUuid, String senderWalletAddress, String receiverWalletAddress) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String fund(UUID fileTransferUuid, String signedTransactionHex, String step) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
