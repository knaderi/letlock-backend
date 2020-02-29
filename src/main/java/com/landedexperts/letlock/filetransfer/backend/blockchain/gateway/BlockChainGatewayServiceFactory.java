/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.blockchain.gateway;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.impl.DBGatewayService;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.impl.GoChainGatewayService;

@Service
public class BlockChainGatewayServiceFactory  implements ApplicationContextAware {

    
    private static ApplicationContext applicationContext;


    public  BlockChainGatewayService createGatewayService(BlockChainGatewayServiceTypeEnum instanceType) {

        if(instanceType == BlockChainGatewayServiceTypeEnum.GOCHAIN_GATEWAY)
            return getBean(GoChainGatewayService.class);
        else if(instanceType == BlockChainGatewayServiceTypeEnum.DB_GATEWAY)
            return getBean(DBGatewayService.class);
        else
            throw new IllegalArgumentException("No such gateway service");
        
    }
    
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }
}
