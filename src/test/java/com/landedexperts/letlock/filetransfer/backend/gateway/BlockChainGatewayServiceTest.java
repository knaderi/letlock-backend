package com.landedexperts.letlock.filetransfer.backend.gateway;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.BackendTestConstants;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceFactory;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceTypeEnum;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.impl.GoChainGatewayService;


public class BlockChainGatewayServiceTest extends AbstractTest implements BackendTestConstants {

    @Autowired
    BlockChainGatewayServiceFactory blockChainGatewayServiceFactory;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testGetGoChainInstance() {
        assertTrue("testGetGoChainInstance: Should have created a GoChain gateway instance", blockChainGatewayServiceFactory.createGatewayService(BlockChainGatewayServiceTypeEnum.GOCHAIN_GATEWAY) instanceof GoChainGatewayService);
    }

}
