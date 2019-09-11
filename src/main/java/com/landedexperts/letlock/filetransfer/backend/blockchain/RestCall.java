package com.landedexperts.letlock.filetransfer.backend.blockchain;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestCall {
    private final static Logger logger = LoggerFactory.getLogger(RestCall.class);

    public static String getWalletAddressFromTransaction(String signedTransactionHex) throws Exception {
        HttpURLConnection askWalletAddress = (HttpURLConnection) new URL("http://localhost:3001/fetch_wallet_address").openConnection();
        askWalletAddress.setDoOutput(true);
        askWalletAddress.setRequestProperty("Content-Type", "application/json");

        OutputStream output = askWalletAddress.getOutputStream();
        output.write(("{\"transaction\":\"" + signedTransactionHex + "\"}").getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();

        InputStream gotWalletAddress = askWalletAddress.getInputStream();
        byte[] answer = new byte[1024];
        int answerLength = gotWalletAddress.read(answer);

        String gotten = new String(answer, 0, answerLength, StandardCharsets.UTF_8);

        logger.info("getWalletAddressFromTransaction" + gotten);

        ObjectMapper mapper = new ObjectMapper();
        WalletAddress walletAddressJson = mapper.readValue(gotten, WalletAddress.class);

        return walletAddressJson.getWalletAddress();
    }

    static public boolean deploySmartContract(UUID fileTransferUuid, String senderWalletAddress, String receiverWalletAddress) throws Exception {
		HttpURLConnection performDeployment = (HttpURLConnection) new URL("http://localhost:3001/deploy_smart_contract").openConnection();
		performDeployment.setDoOutput(true);
		performDeployment.setRequestProperty("Content-Type", "application/json");

		OutputStream output = performDeployment.getOutputStream();
		output.write(("{"
						+ "\"fileTransferUuid\":\"" + fileTransferUuid.toString() + "\","
						+ "\"senderAddress\":\"" + senderWalletAddress + "\","
						+ "\"receiverAddress\":\"" + receiverWalletAddress + "\""
					+ "}").getBytes(StandardCharsets.UTF_8));
		output.flush();
		output.close();

		InputStream gotReceipt = performDeployment.getInputStream();
		byte[] answer = new byte[1024];
		int answerLength = gotReceipt.read(answer);

		String gotten = new String(answer, 0, answerLength, StandardCharsets.UTF_8);

		logger.info("deploySmartContract " + gotten);

		ObjectMapper mapper = new ObjectMapper();
		ResultJson emptyJson = mapper.readValue(gotten, ResultJson.class);

		return emptyJson.getResult();
	}

    static public String fund(UUID fileTransferUuid, String signedTransactionHex, String step) throws Exception {
        HttpURLConnection fundIt = (HttpURLConnection) new URL("http://localhost:3001/fund").openConnection();
        fundIt.setDoOutput(true);
        fundIt.setRequestProperty("Content-Type", "application/json");

        OutputStream output = fundIt.getOutputStream();
        output.write(("{" + "\"fileTransferUuid\":\"" + fileTransferUuid.toString() + "\"," + "\"transaction\":\"" + signedTransactionHex
                + "\"," + "\"step\":\"" + step + "\"" + "}").getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();

        InputStream gotTxHash = fundIt.getInputStream();
        byte[] answer = new byte[1024];
        int answerLength = gotTxHash.read(answer);

        String gotten = new String(answer, 0, answerLength, StandardCharsets.UTF_8);

        
        logger.info("fund " + gotten);

        ObjectMapper mapper = new ObjectMapper();
        TransactionHashJson transactionHashJson = mapper.readValue(gotten, TransactionHashJson.class);

        return transactionHashJson.getTransactionHash();
    }
}
