package com.landedexperts.letlock.filetransfer.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayService;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceFactory;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceTypeEnum;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.OrderMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.TransactionHashResponse;


@RestController
public class UtilsController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    OrderMapper orderMapper;
    
    @Autowired
    BlockChainGatewayServiceFactory blockChainGatewayServiceFactory;
    
    @Value("${blockchain.gateway.type}")
    private String blockchainGatewayType;
    
    @GetMapping(value = "/health")
    public ResponseEntity<String> healthCheck() throws Exception {
        HttpStatus status = HttpStatus.OK;
        String body = "";
        // Trying to get packages to check DB connection
        try {
            String value = orderMapper.getPackages(false, false);
        } catch(Exception e) {
            logger.info("DB connection check error: {}", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = "DB connection check error: " + e.getMessage() + "\n";
        }
        // Trying to get transaction status to check Blockchain connection
        try {
            TransactionHashResponse transactionHashResponse = getBlockChainGateWayService()
                    .getTransactionStatus("0xabcdef0123456789abcdef0123456789abcdef0123456789abcdef0123456789");
        } catch (Exception e) {
            logger.info("Blockchain engine check error: {}", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = body + "Blockchain engine check error: " + e.getMessage();
        }
        return ResponseEntity.status(status).body(body);
    }
    
    private BlockChainGatewayService getBlockChainGateWayService() {
        BlockChainGatewayServiceTypeEnum blockchainGatewayServiceType = BlockChainGatewayServiceTypeEnum
                .fromValue(blockchainGatewayType);
        return blockChainGatewayServiceFactory.createGatewayService(blockchainGatewayServiceType);
    }
}
