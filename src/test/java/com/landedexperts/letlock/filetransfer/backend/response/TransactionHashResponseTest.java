package com.landedexperts.letlock.filetransfer.backend.response;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.TransactionHashResponse;

public class TransactionHashResponseTest extends AbstractTest {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testTransactionResponseCreation() throws Exception {
        String responseStr = "{\"returnCode\":\"TXN_NOT_FOUND\",\"returnMessage\":\"Transaction hash not found\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TransactionHashResponse value = objectMapper.readValue(responseStr, TransactionHashResponse.class);
        assertEquals("testTransactionResponseCreation: Should get TXT_NOT_FOUND", "TXN_NOT_FOUND", value.getReturnCode());
        assertEquals("testTransactionResponseCreation: Should get transcation hash not found", "Transaction hash not found",
                value.getReturnMessage());

    }

    @Test
    public void testTransactionResponseCreation2() throws Exception {
        String responseStr = "{\"returnCode\":\"TXN_NOT_FOUND\",\"returnMessage\":\"Transaction hash not found\", \"status\":\"completed\",\"transactionHash\":\"0xsdfdsfdssd\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TransactionHashResponse value = objectMapper.readValue(responseStr, TransactionHashResponse.class);
        assertEquals("testTransactionResponseCreation2:  Should get TXT_NOT_FOUND", "TXN_NOT_FOUND", value.getReturnCode());
        assertEquals("testTransactionResponseCreation2:  Should get transcation hash not found", "Transaction hash not found",
                value.getReturnMessage());
        assertEquals("testTransactionResponseCreation2: Should get completed", "completed", value.getStatus());
        assertEquals("testTransactionResponseCreation2: Should get the correct transaction value", "0xsdfdsfdssd",
                value.getTransactionHash());
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
        assertEquals("testTransactionResponseCreation3:", "SUCCESS", value.getReturnCode());
        assertEquals("testTransactionResponseCreation3:", "", value.getReturnMessage());
        assertEquals("testTransactionResponseCreation3:", "completed", value.getStatus());
        assertEquals("testTransactionResponseCreation3:", "0xfbe41c0a01eca13ed0b894e10d073e80cd44e06f0d15343f877bada45472c5ff",
                value.getTransactionHash());
    }

}
