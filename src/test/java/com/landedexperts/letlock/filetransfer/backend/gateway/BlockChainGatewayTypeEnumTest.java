package com.landedexperts.letlock.filetransfer.backend.gateway;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceTypeEnum;

public class BlockChainGatewayTypeEnumTest extends AbstractTest {
    
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testGetValue() {
        assertEquals("gochain-gateway", BlockChainGatewayServiceTypeEnum.GOCHAIN_GATEWAY.getValue());
        assertEquals("db-gateway", BlockChainGatewayServiceTypeEnum.DB_GATEWAY.getValue());      
    }
    
    @Test
    public void testEnum() {
        assertEquals(BlockChainGatewayServiceTypeEnum.GOCHAIN_GATEWAY, BlockChainGatewayServiceTypeEnum.fromValue("gochain-gateway"));
        assertEquals(BlockChainGatewayServiceTypeEnum.DB_GATEWAY, BlockChainGatewayServiceTypeEnum.fromValue("db-gateway") );
        assertNull(BlockChainGatewayServiceTypeEnum.fromValue("invalid-value") ); 
    }
    
}
