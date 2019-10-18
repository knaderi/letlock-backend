package com.landedexperts.letlock.filetransfer.backend.blockchain.gateway;

import org.springframework.stereotype.Service;

import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.impl.DBGatewayService;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.impl.GoChainGatewayService;

@Service
public class BlockChainGatewayServiceFactory {

    
    private static BlockChainGatewayService instance;

    public BlockChainGatewayServiceFactory() {

    }
    
    public  BlockChainGatewayService createGatewayService(BlockChainGatewayServiceTypeEnum instanceType) {
        if(null != instance) {
            return instance;
        }
        if(instanceType == BlockChainGatewayServiceTypeEnum.GOCHAIN_GATEWAY)
            instance = new GoChainGatewayService();
        else if(instanceType == BlockChainGatewayServiceTypeEnum.DB_GATEWAY)
            instance = new DBGatewayService();
        else
            throw new IllegalArgumentException("No such gateway service");
        
        return instance;

    }
}
