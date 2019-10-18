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
        assertEquals("completed", value.getStatus());
        assertEquals("0xsdfdsfdssd", value.getTransactionHash());
    }
    
    @Test
    public void testTransactionResponseCreation3() throws Exception {
        String responseStr = "{\r\n" + 
        		"    \"errorCode\": \"NO_ERROR\",\r\n" + 
        		"    \"errorMessage\": \"\",\r\n" + 
        		"    \"transactionHash\": \"0xfbe41c0a01eca13ed0b894e10d073e80cd44e06f0d15343f877bada45472c5ff\",\r\n" + 
        		"    \"status\": \"completed\"\r\n" + 
        		"}";
        ObjectMapper objectMapper = new ObjectMapper();
        TransactionHashResponse value = objectMapper.readValue(responseStr, TransactionHashResponse.class);
        assertEquals("NO_ERROR", value.getErrorCode());
        assertEquals("", value.getErrorMessage());
        assertEquals("completed", value.getStatus());
        assertEquals("0xfbe41c0a01eca13ed0b894e10d073e80cd44e06f0d15343f877bada45472c5ff", value.getTransactionHash());
    }

}
