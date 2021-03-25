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
import com.landedexperts.letlock.filetransfer.backend.utils.AdminNotification;
import com.landedexperts.letlock.filetransfer.backend.utils.AppAccountBalance;


@RestController
public class UtilsController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    OrderMapper orderMapper;
    
    @Autowired
    BlockChainGatewayServiceFactory blockChainGatewayServiceFactory;
    
    @Autowired
    AdminNotification adminNotification;
    
    @Value("${blockchain.gateway.type}")
    private String blockchainGatewayType;
    
    @Value("${app.blockchain.account.threshold}")
    private int appAccountThreshold;
    
    @GetMapping(value = "/health")
    public ResponseEntity<String> healthCheck() throws Exception {
        HttpStatus status = HttpStatus.OK;
        String body = "";
        // Trying to get packages to check DB connection
        try {
            @SuppressWarnings("unused")
            String value = orderMapper.getPackages(false, false);
        } catch(Exception e) {
            logger.info("DB connection check error: {}", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = "DB connection check error: " + e.getMessage() + "\n";
        }
        // Trying to get the LetLock GoCahin account balance
        try {
            AppAccountBalance balanceInfo = getBlockChainGateWayService().getAppAccountBalance();
            float balance = Float.parseFloat(balanceInfo.getBalance());
            if (balance < appAccountThreshold) {
                adminNotification.appBalanceThresholdReached(balanceInfo);
            }
        } catch (Exception e) {
            logger.info("Blockchain engine check error: {}", e.getMessage());
            adminNotification.actionFailure("Get balance for LetLock GoChain account", e);
        }
        return ResponseEntity.status(status).body(body);
    }
    
    private BlockChainGatewayService getBlockChainGateWayService() {
        BlockChainGatewayServiceTypeEnum blockchainGatewayServiceType = BlockChainGatewayServiceTypeEnum
                .fromValue(blockchainGatewayType);
        return blockChainGatewayServiceFactory.createGatewayService(blockchainGatewayServiceType);
    }
}
