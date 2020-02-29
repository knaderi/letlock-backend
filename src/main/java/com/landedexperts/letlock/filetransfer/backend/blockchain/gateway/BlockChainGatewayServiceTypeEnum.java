/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.blockchain.gateway;

public enum BlockChainGatewayServiceTypeEnum {
    
    GOCHAIN_GATEWAY("gochain_gateway"),
    DB_GATEWAY("db_gateway");
    
    private String gatewayType;
    
    BlockChainGatewayServiceTypeEnum(String gatewayType){
        this.gatewayType = gatewayType;
    }
    
    public static BlockChainGatewayServiceTypeEnum fromValue(String value) {
        for (BlockChainGatewayServiceTypeEnum e : values()) {
            if (e.gatewayType.equals(value)) {
                return e;
            }
        }
        return null;
    }
    
    public String getValue() {
        return this.gatewayType;
    }
}
