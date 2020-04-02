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

import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceTypeEnum;

public class BlockChainGatewayTypeEnumTest extends AbstractTest {
    
    @Override
    @BeforeEach
    public void setUp() throws Exception{
        super.setUp();
    }
    
    @Test
    public void testGetValue() {
        Assertions.assertEquals(BlockChainGatewayServiceTypeEnum.GOCHAIN_GATEWAY.getValue(),"gochain_gateway", "testGetValue: It should be gochain-gateway");
        Assertions.assertEquals("db_gateway", BlockChainGatewayServiceTypeEnum.DB_GATEWAY.getValue(), "testGetValue: It should be db-gateway");      
    }
    
    @Test
    public void testEnum() {
        Assertions.assertEquals(BlockChainGatewayServiceTypeEnum.GOCHAIN_GATEWAY, BlockChainGatewayServiceTypeEnum.fromValue("gochain_gateway"),"testEnum: It should be GoChian Gateway");
        Assertions.assertEquals(BlockChainGatewayServiceTypeEnum.DB_GATEWAY, BlockChainGatewayServiceTypeEnum.fromValue("db_gateway"),"testEnum: It should be DB Gateway" );
        Assertions.assertNull(BlockChainGatewayServiceTypeEnum.fromValue("invalid-value"),"testEnum: It should be invalid value"); 
    }
    
}
