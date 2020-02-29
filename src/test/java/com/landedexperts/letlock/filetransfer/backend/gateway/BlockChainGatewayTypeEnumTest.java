/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.gateway;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceTypeEnum;

public class BlockChainGatewayTypeEnumTest extends AbstractTest {
    
    @Override
    @Before
    public void setUp() throws Exception{
        super.setUp();
    }
    
    @Test
    public void testGetValue() {
        assertEquals("testGetValue: It should be gochain-gateway", "gochain_gateway", BlockChainGatewayServiceTypeEnum.GOCHAIN_GATEWAY.getValue());
        assertEquals("testGetValue: It should be db-gateway", "db_gateway", BlockChainGatewayServiceTypeEnum.DB_GATEWAY.getValue());      
    }
    
    @Test
    public void testEnum() {
        assertEquals("testEnum: It should be GoChian Gateway",BlockChainGatewayServiceTypeEnum.GOCHAIN_GATEWAY, BlockChainGatewayServiceTypeEnum.fromValue("gochain_gateway"));
        assertEquals("testEnum: It should be DB Gateway",BlockChainGatewayServiceTypeEnum.DB_GATEWAY, BlockChainGatewayServiceTypeEnum.fromValue("db_gateway") );
        assertNull("testEnum: It should be invalid value",BlockChainGatewayServiceTypeEnum.fromValue("invalid-value") ); 
    }
    
}
