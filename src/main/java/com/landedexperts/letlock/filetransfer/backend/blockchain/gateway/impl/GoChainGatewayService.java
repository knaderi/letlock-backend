/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.landedexperts.letlock.filetransfer.backend.blockchain.ResultJson;
import com.landedexperts.letlock.filetransfer.backend.blockchain.TransactionHashJson;
import com.landedexperts.letlock.filetransfer.backend.blockchain.WalletAddress;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayService;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceTypeEnum;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.TransactionHashResponse;

@Service
public class GoChainGatewayService extends BlockChainGatewayService {
    private final static Logger logger = LoggerFactory.getLogger(GoChainGatewayService.class);
    
    @Value("${gochain.engine.proxy.url}")
    private String goChainURL;
    /*
     * (non-Javadoc)
     * 
     * @see com.landedexperts.letlock.filetransfer.backend.blockchain.
     * IBlockChainGatewayService#getWalletAddressFromTransaction(java.lang.String)
     */
    @Override
    public String getWalletAddressFromTransaction(UUID fileTransferUuid, String transaction, String step) throws Exception {
        HttpURLConnection urlConnection = getURLConnection(goChainURL +"/fetch_wallet_address");
        WalletAddress walletAddressJson = null;
        try {
            OutputStream output = urlConnection.getOutputStream();
            output.write(("{\"transaction\":\"" + transaction + "\"}").getBytes(StandardCharsets.UTF_8));
            String responseStr = readInputStreamIntoJsonString(urlConnection);
            logger.debug("getWalletAddressFromTransaction" + responseStr);
            walletAddressJson = new ObjectMapper().readValue(responseStr, WalletAddress.class);
        } finally {
            cleanUpStreams(urlConnection);
        }
        return walletAddressJson.getWalletAddress();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.landedexperts.letlock.filetransfer.backend.blockchain.
     * IBlockChainGatewayService#gTransactionHash(java.lang.String)
     */
    @Override
    public TransactionHashResponse getTransactionStatus(String transactionHash) throws Exception {
        HttpURLConnection urlConnection = getURLConnection(goChainURL + "/get_txn_status");
        TransactionHashResponse transactionHashResponse;
        String responseStr;
        try {
            OutputStream output = urlConnection.getOutputStream();
            output.write(("{\"transactionHash\":\"" + transactionHash + "\"}").getBytes(StandardCharsets.UTF_8));

            responseStr = readInputStreamIntoJsonString(urlConnection);
            logger.debug("searchTransactionHash" + responseStr);
            ObjectMapper objectMapper = new ObjectMapper();            
            transactionHashResponse = objectMapper.readValue(responseStr, TransactionHashResponse.class);            
        } finally {
            cleanUpStreams(urlConnection);
        }
        return transactionHashResponse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.landedexperts.letlock.filetransfer.backend.blockchain.
     * IBlockChainGatewayService#deploySmartContract(java.util.UUID,
     * java.lang.String, java.lang.String)
     */
    @Override
    public boolean deploySmartContract(UUID fileTransferUuid, String senderWalletAddress, String receiverWalletAddress) throws Exception {
        logger.info("GoChainGatewayService.deploySmartContract  called to deploy smart contract for fileTransferUuid " + fileTransferUuid);
        HttpURLConnection urlConnection = getURLConnection(goChainURL + "/deploy_smart_contract");
        ResultJson emptyJson = null;
        try {
            OutputStream output = urlConnection.getOutputStream();
            output.write(
                    ("{" + "\"fileTransferUuid\":\"" + fileTransferUuid.toString() + "\"," + "\"senderAddress\":\"" + senderWalletAddress
                            + "\"," + "\"receiverAddress\":\"" + receiverWalletAddress + "\"" + "}").getBytes(StandardCharsets.UTF_8));
            String responseStr = readInputStreamIntoJsonString(urlConnection);
            logger.debug("deploySmartContract " + responseStr);
            emptyJson = new ObjectMapper().readValue(responseStr, ResultJson.class);
        } finally {
            cleanUpStreams(urlConnection);
        }
        return emptyJson.getResult();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.landedexperts.letlock.filetransfer.backend.blockchain.
     * IBlockChainGatewayService#fund(java.util.UUID, java.lang.String,
     * java.lang.String)
     */
    @Override
    public String fund(UUID fileTransferUuid, String signedTransactionHex, String step) throws Exception {
        HttpURLConnection urlConnection = getURLConnection(goChainURL + "/fund");
        TransactionHashJson transactionHashJson = null;
        try {
            OutputStream output = urlConnection.getOutputStream();
            output.write(("{" + "\"fileTransferUuid\":\"" + fileTransferUuid.toString() + "\"," + "\"transaction\":\""
                    + signedTransactionHex + "\"," + "\"step\":\"" + step + "\"" + "}").getBytes(StandardCharsets.UTF_8));

            String responseStr = readInputStreamIntoJsonString(urlConnection);
            logger.debug("fund " + responseStr);
            transactionHashJson = new ObjectMapper().readValue(responseStr, TransactionHashJson.class);
        } finally {
            cleanUpStreams(urlConnection);
        }
        return transactionHashJson.getTransactionHash();
    }

    private HttpURLConnection getURLConnection(String url) throws IOException, MalformedURLException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        return urlConnection;
    }

    private String readInputStreamIntoJsonString(HttpURLConnection urlConnection) throws IOException {
        InputStream inputStream = null;
        String jsonString;
        inputStream = urlConnection.getInputStream();
        byte[] answer = new byte[1024];
        int answerLength = inputStream.read(answer);
        jsonString = new String(answer, 0, answerLength, StandardCharsets.UTF_8);
        return jsonString;
    }

    private void cleanUpStreams(HttpURLConnection urlConnection) throws IOException {
        try {
            if (null != urlConnection && urlConnection.getInputStream() != null) {
                urlConnection.getInputStream().close();
                if (urlConnection.getOutputStream() != null) {
                    urlConnection.getOutputStream().flush();
                    urlConnection.getOutputStream().close();
                }
            }
        } catch (Exception e) {
            logger.error("Error closing Stream " + e.getMessage());
        }

    }

    @Override
    public String getType() throws Exception {
        return BlockChainGatewayServiceTypeEnum.GOCHAIN_GATEWAY.getValue();
    }

}
