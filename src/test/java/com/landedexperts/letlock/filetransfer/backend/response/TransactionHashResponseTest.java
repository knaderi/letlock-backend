/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.TransactionHashResponse;

public class TransactionHashResponseTest extends AbstractTest {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testTransactionResponseCreation() throws Exception {
        String responseStr = "{\"returnCode\":\"TXN_NOT_FOUND\",\"returnMessage\":\"Transaction hash not found\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TransactionHashResponse value = objectMapper.readValue(responseStr, TransactionHashResponse.class);
        Assertions.assertEquals("TXN_NOT_FOUND", value.getReturnCode(), "testTransactionResponseCreation: Should get TXT_NOT_FOUND");
        Assertions.assertEquals("Transaction hash not found",
                value.getReturnMessage(), "testTransactionResponseCreation: Should get transcation hash not found");

    }

    @Test
    public void testTransactionResponseCreation2() throws Exception {
        String responseStr = "{\"returnCode\":\"TXN_NOT_FOUND\",\"returnMessage\":\"Transaction hash not found\", \"status\":\"completed\",\"transactionHash\":\"0xsdfdsfdssd\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TransactionHashResponse value = objectMapper.readValue(responseStr, TransactionHashResponse.class);
        Assertions.assertEquals("TXN_NOT_FOUND", value.getReturnCode(), "testTransactionResponseCreation2:  Should get TXT_NOT_FOUND");
        Assertions.assertEquals("Transaction hash not found",
                value.getReturnMessage(), "testTransactionResponseCreation2:  Should get transcation hash not found");
        Assertions.assertEquals("completed", value.getStatus(),"testTransactionResponseCreation2: Should get completed");
        Assertions.assertEquals("0xsdfdsfdssd",
                value.getTransactionHash(), "testTransactionResponseCreation2: Should get the correct transaction value");
    }

    @Test
    public void testTransactionResponseCreation3() throws Exception {
        String responseStr = "{\r\n" +
                "    \"returnCode\": \"SUCCESS\",\r\n" +
                "    \"returnMessage\": \"\",\r\n" +
                "    \"transactionHash\": \"0xfbe41c0a01eca13ed0b894e10d073e80cd44e06f0d15343f877bada45472c5ff\",\r\n" +
                "    \"status\": \"completed\"\r\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        TransactionHashResponse value = objectMapper.readValue(responseStr, TransactionHashResponse.class);
        Assertions.assertEquals("SUCCESS", value.getReturnCode(), "testTransactionResponseCreation3:The return code should be SUCCESS");
        Assertions.assertEquals("", value.getReturnMessage(), "testTransactionResponseCreation3:The return messge should be empty");
        Assertions.assertEquals("completed", value.getStatus(),"testTransactionResponseCreation3:Transaction status should be completed");
        Assertions.assertEquals("0xfbe41c0a01eca13ed0b894e10d073e80cd44e06f0d15343f877bada45472c5ff",
                value.getTransactionHash(), "testTransactionResponseCreation3: Retrived transaction hash is not equal to the expected value");
    }

}
