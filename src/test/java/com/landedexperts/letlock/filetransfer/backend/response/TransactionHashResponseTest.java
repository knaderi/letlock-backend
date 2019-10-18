package com.landedexperts.letlock.filetransfer.backend.response;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.junit.Before;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.landedexperts.letlock.filetransfer.backend.AbstractTest;

public class TransactionHashResponseTest extends AbstractTest {
    
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testTransactionResponseCreation() throws Exception {
        String responseStr = "{\"errorCode\":\"TXN_NOT_FOUND\",\"errorMessage\":\"Transaction hash not found\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TransactionHashResponse value = objectMapper.readValue(responseStr, TransactionHashResponse.class);
        assertEquals("TXN_NOT_FOUND", value.getErrorCode());
        assertEquals("Transaction hash not found", value.getErrorMessage());

    }

    @Test
    public void testTransactionResponseCreation2() throws Exception {
        String responseStr = "{\"errorCode\":\"TXN_NOT_FOUND\",\"errorMessage\":\"Transaction hash not found\", \"status\":\"completed\",\"transactionHash\":\"0xsdfdsfdssd\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TransactionHashResponse value = objectMapper.readValue(responseStr, TransactionHashResponse.class);
        assertEquals("TXN_NOT_FOUND", value.getErrorCode());
        assertEquals("Transaction hash not found", value.getErrorMessage());
        assertEquals("TXN_NOT_FOUND", value.getErrorCode());
        assertEquals("completed", value.getStatus());
        assertEquals("0xsdfdsfdssd", value.getTransactionHash());
    }

}
