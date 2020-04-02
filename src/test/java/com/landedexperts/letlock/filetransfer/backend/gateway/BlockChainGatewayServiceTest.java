/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.gateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testGetGoChainInstance() {
        Assertions.assertTrue(blockChainGatewayServiceFactory.createGatewayService(BlockChainGatewayServiceTypeEnum.GOCHAIN_GATEWAY) instanceof GoChainGatewayService, "testGetGoChainInstance: Should have created a GoChain gateway instance");
    }

}
