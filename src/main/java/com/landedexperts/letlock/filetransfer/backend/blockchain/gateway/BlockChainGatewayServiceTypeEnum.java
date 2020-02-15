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
