package com.landedexperts.letlock.filetransfer.backend.blockchain;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestCall {
	static public String getWalletAddressFromTransaction(String signedTransactionHex) throws Exception {
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

		System.out.println("getWalletAddressFromTransaction " + gotten);

		ObjectMapper mapper = new ObjectMapper();
		WalletAddress walletAddressJson = mapper.readValue(gotten, WalletAddress.class);

		return walletAddressJson.getWalletAddress();
	}

	static public String deploySmartContract(String senderWalletAddress, String receiverWalletAddress) throws Exception {
		HttpURLConnection performDeployment = (HttpURLConnection) new URL("http://localhost:3001/deploy_smart_contract").openConnection();
		performDeployment.setDoOutput(true);
		performDeployment.setRequestProperty("Content-Type", "application/json");

		OutputStream output = performDeployment.getOutputStream();
		output.write(("{"
						+ "\"senderAddress\":\"" + senderWalletAddress + "\","
						+ "\"receiverAddress\":\"" + receiverWalletAddress + "\""
					+ "}").getBytes(StandardCharsets.UTF_8));
		output.flush();
		output.close();

		InputStream gotReceipt = performDeployment.getInputStream();
		byte[] answer = new byte[1024];
		int answerLength = gotReceipt.read(answer);

		String gotten = new String(answer, 0, answerLength, StandardCharsets.UTF_8);

		System.out.println("deploySmartContract " + gotten);

		ObjectMapper mapper = new ObjectMapper();
		Receipt receiptJson = mapper.readValue(gotten, Receipt.class);

		return receiptJson.getContractAddress();
	}

	static public String fund(String signedTransactionHex) throws Exception {
		HttpURLConnection fundIt = (HttpURLConnection) new URL("http://localhost:3001/fund").openConnection();
		fundIt.setDoOutput(true);
		fundIt.setRequestProperty("Content-Type", "application/json");

		OutputStream output = fundIt.getOutputStream();
		output.write(("{\"transaction\":\"" + signedTransactionHex + "\"}").getBytes(StandardCharsets.UTF_8));
		output.flush();
		output.close();

		InputStream gotReceipt = fundIt.getInputStream();
		byte[] answer = new byte[1024];
		int answerLength = gotReceipt.read(answer);

		String gotten = new String(answer, 0, answerLength, StandardCharsets.UTF_8);

		System.out.println("fund " + gotten);

		ObjectMapper mapper = new ObjectMapper();
		Receipt receiptJson = mapper.readValue(gotten, Receipt.class);

		return receiptJson.getContractAddress();
	}
}
