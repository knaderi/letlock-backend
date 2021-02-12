/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayService;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceFactory;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceTypeEnum;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.FileTransferMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ConsumeResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.FileTransferSessionResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.FileTransferSessionsResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.TransactionHashResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.UuidNameDate;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.UuidNameDateArrayResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.UuidResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FileTransferInfoRecordVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.FileTransferInfoVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.GochainAddressVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.UuidNameDateVO;

@RestController
public class FileTransferController {
    @Autowired
    FileTransferMapper fileTransferMapper;

    @Value("${blockchain.gateway.type}")
    private String blockchainGatewayType;

    @Autowired
    BlockChainGatewayServiceFactory blockChainGatewayServiceFactory;

    private final Logger logger = LoggerFactory.getLogger(FileTransferController.class);

    @PostMapping(value = "/start_file_transfer_session", produces = { "application/JSON" })
    public ConsumeResponse startFileTransferSession(
            @RequestParam(value = "wallet_address") final String walletAddress,
            @RequestParam(value = "receiver_login_name") final String receiverLoginName,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute("user.id");
        logger.info("FileTransferController.startFileTransferSession called for userId " + userId);

        String walletAddressTrimmed = walletAddress.substring(0, 2).equals("0x") ? walletAddress.substring(2)
                : walletAddress;

        FileTransferInfoVO answer = fileTransferMapper.insertFileTransferSessionRecord(userId,
                walletAddressTrimmed, receiverLoginName, getBlockChainGateWayService().getType());

        String fileTransferUuid = answer.getFileTransferUuid();
        String walletAddressUuid = answer.getReceiverWalletAddressUuid();
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();

        return new ConsumeResponse(fileTransferUuid, walletAddressUuid, returnCode, returnMessage);
    }

    @PostMapping(value = "/is_file_transfer_waiting_receiver_address", produces = { "application/JSON" })
    public UuidNameDateArrayResponse isFileTransferWaitingForReceiverAddress(
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute("user.id");
        logger.info("FileTransferController.isFileTransferWaitingForReceiverAddress called for userId " + userId);

        UuidNameDateVO[] answer = fileTransferMapper.retrieveSessionWaitingForReceiverAddress(userId);

        UuidNameDate[] value = new UuidNameDate[answer.length];
        for (int i = 0; i < answer.length; i++) {
            value[i] = new UuidNameDate(UUID.fromString(answer[i].getUuid()), answer[i].getName(),
                    answer[i].getCreate());
        }

        return new UuidNameDateArrayResponse(value, "SUCCESS", "");
    }

    // TODO: write unit test for this.
    @PostMapping(value = "/set_file_transfer_active", produces = { "application/JSON" })
    public ReturnCodeMessageResponse setFileTransferActive(
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute("user.id");
        logger.info("FileTransferController.setFileTransferActive called for userId " + userId);

        ReturnCodeMessageResponse answer = fileTransferMapper.setFileTransferAsActive(userId, fileTransferUuid);

        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();

        return new ReturnCodeMessageResponse(returnCode, returnMessage);
    }

    // TODO: write unit test for this.
    @PostMapping(value = "/set_file_transfer_inactive", produces = { "application/JSON" })
    public ReturnCodeMessageResponse setFileTransferInactive(
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute("user.id");
        logger.info("FileTransferController.setFileTransferInactive called for userId " + userId);
        
        ReturnCodeMessageResponse answer = fileTransferMapper.setFileTransferInactive(userId, fileTransferUuid);
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();

        return new ReturnCodeMessageResponse(returnCode, returnMessage);
    }

    @PostMapping(value = "/set_file_transfer_file_hashes", produces = { "application/JSON" })
    public ReturnCodeMessageResponse setFileTransferFileHashes(
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid,
            @RequestParam(value = "clearFileHash") final String clearFileHash,
            @RequestParam(value = "encryptedFileHash") final String encryptedFileHash,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute("user.id");
        logger.info("FileTransferController.setFileTransferFilesHash called for userId " + userId);

        ReturnCodeMessageResponse answer = fileTransferMapper.setFileTransferFileHashes(
                userId, fileTransferUuid, clearFileHash, encryptedFileHash);
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();

        return new ReturnCodeMessageResponse(returnCode, returnMessage);
    }

    @PostMapping(value = "/get_file_transfer_sessions_for_user", produces = { "application/JSON" })
    public FileTransferSessionsResponse getFileTransferSessionsForUser(
            HttpServletRequest request) throws Exception {
 
        long userId = (long) request.getAttribute("user.id");
        FileTransferInfoRecordVO[] value = fileTransferMapper.getFileTransferSessionsForUser(userId);

        return new FileTransferSessionsResponse(value, "SUCCESS", "");
    }

    // TODO: write unit test
    @PostMapping(value = "/get_file_transfer_status", produces = { "application/JSON" })
    public FileTransferSessionResponse getFileTransferStatus(
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute("user.id");
        logger.info("FileTransferController.getFileTransferStatus called for userId " + userId);

        FileTransferInfoVO answer = fileTransferMapper.getUserFileTransferInfo(userId, fileTransferUuid);
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();

        logger.info("Returrning returnCode: " + returnCode + "  record " + answer.getFileTransferInfoRecord());

        return new FileTransferSessionResponse(answer.getFileTransferInfoRecord(), returnCode, returnMessage);
    }

    // TODO: Missing UNit test
    @PostMapping(value = "/set_file_transfer_receiver_address", produces = { "application/JSON" })
    public UuidResponse setFileTransferReceiverAddress(
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid,
            @RequestParam(value = "wallet_address") final String walletAddress,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute("user.id");
        logger.info("FileTransferController.setFileTransferReceiverAddress called for userId " + userId);


        String walletAddressTrimmed = walletAddress.substring(0, 2).equals("0x") ? walletAddress.substring(2)
                : walletAddress;

        GochainAddressVO answer = fileTransferMapper.setReceiverAddress(userId, fileTransferUuid,
                walletAddressTrimmed);

        UUID walletAddressUuid = answer.getGochainAddress();
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();

        if (returnCode.equals("SUCCESS")) {
            FileTransferInfoVO fileTransferInfo = fileTransferMapper.getUserFileTransferInfo(userId,
                    fileTransferUuid);
            String contractAddress = fileTransferInfo.getSmartContractAddress();
            if (StringUtils.isEmpty(contractAddress)) {
                String senderWalletAddress = fileTransferInfo.getSenderWalletAddress();
                String receiverWalletAddress = fileTransferInfo.getReceiverWalletAddress();
                logger.info("FileTransferController.setFileTransferReceiverAddress is deploying smart contarct for Filetransferuid: "
                        + fileTransferUuid);
                try {
                    @SuppressWarnings("unused")
                    boolean response = getBlockChainGateWayService().deploySmartContract(fileTransferUuid,
                            "0x" + senderWalletAddress, "0x" + receiverWalletAddress);
                    if (!response) {
                        throw new Exception("DeploySmartContract returned false");
                    }

                } catch (Exception e) {
                    if (e instanceof java.net.ConnectException) {
                        returnCode = "BLOCKCHAIN_CONNECTION_EXCEPTION";
                        returnMessage = "Connecting to Blockchain failed trying to deploy smart contract.";

                    } else {
                        returnCode = "BLOCKCHAIN_UKNOWN_EXCEPTION";
                        returnMessage = "Deploy smart contract faild throwing an Exception.";
                    }
                    logger.error("Deploy smart contract failed returnCode: {} returnMessage: {}   Exception: {}", returnCode,
                            returnMessage, e.getMessage());
                    return new UuidResponse(walletAddressUuid, returnCode, returnMessage);
                }
            } else {
                logger.info("smart contract was deployed and set previously.");
            }
        }

        return new UuidResponse(walletAddressUuid, returnCode, returnMessage);
    }

    @PostMapping(value = "/get_txn_status", produces = { "application/JSON" })
    public TransactionHashResponse searchTransactionHash(
            @RequestParam(value = "transactionHash") final String transactionHash) throws Exception {
        logger.info("FileTransferController.searchTransactionHash called for transactionHash " + transactionHash);
        String returnCode = "";
        String returnMessage = "";
        TransactionHashResponse transactionHashResponse = null;

        try {
            transactionHashResponse = getBlockChainGateWayService().getTransactionStatus(transactionHash);
        } catch (Exception e) {
            if (e instanceof java.net.ConnectException) {
                returnCode = "GOCHAIN_CONNECTION_EXCEPTION";
                returnMessage = "Connecting to Blockchain failed trying to get transaction status.";

            } else {
                returnCode = "GOCHAIN_UKNOWN_EXCEPTION";
                returnMessage = "Retrieving transaction status failed  throwing an Exception.";
            }
            logger.error("Retrieving transaction status failed returnCode: {}  returnMessage:  {}   Exception:{}", returnCode,
                    returnMessage, e.getMessage());
            transactionHashResponse = new TransactionHashResponse(returnCode, returnMessage);
        }

        return transactionHashResponse;
    }

    @PostMapping(value = "/add_funds", produces = { "application/JSON" })
    public TransactionHashResponse addFunds(@RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid,
            @RequestParam(value = "signed_transaction_hex") final String signedTransactionHex,
            @RequestParam(value = "step") final String step) throws Exception {

        logger.info("FileTransferController.addFunds called for file_transfer_uuid " + fileTransferUuid);
        String returnCode = "";
        String returnMessage = "";
        String walletAddress = "";
        try {
            walletAddress = getBlockChainGateWayService().getWalletAddressFromTransaction(fileTransferUuid,
                    signedTransactionHex, step);

        } catch (Exception e) {
            if (e instanceof java.net.ConnectException) {
                returnCode = "BLOCKCHAIN_CONNECTION_EXCEPTION";
                returnMessage = "Connecting to Blockchain failed trying to get wallet address from transaction.";

            } else {
                returnCode = "BLOCKCHAIN_UKNOWN_EXCEPTION";
                returnMessage = "Retrieving wallet address from transaction failed  throwing an Exception.";
            }
            logger.error("Retrieving wallet address from transaction failed  returnCode: {}  returnMessage:  {}  Exception: {}",
                    returnCode, returnMessage, e.getMessage());
            return new TransactionHashResponse("", returnCode, returnMessage);
        }

        walletAddress = remove0xPrefix(walletAddress);

        BooleanResponse canStart = fileTransferMapper.canStartStep(fileTransferUuid, walletAddress,
                step);

        returnCode = canStart.getReturnCode();
        returnMessage = canStart.getReturnMessage();

        String transactionHash = "";
        if (returnCode.equals("SUCCESS") && canStart.getResult().getValue()) {
            logger.info("FileTransferController.addFunds calling set transfer funding step pending " + fileTransferUuid);
            fileTransferMapper.fileTransferSetTransferStepPending(fileTransferUuid, walletAddress, step);
            try {
                transactionHash = getBlockChainGateWayService().fund(fileTransferUuid, signedTransactionHex, step);
            } catch (Exception e) {
                if (e instanceof java.net.ConnectException) {
                    returnCode = "BLOCKCHAIN_CONNECTION_EXCEPTION";
                    returnMessage = "Connecting to Blockchain failed trying to get wallet address from transaction.";

                } else {
                    returnCode = "BLOCKCHAIN_UKNOWN_EXCEPTION";
                    returnMessage = "Retrieving wallet address from transaction failed  throwing an Exception.";
                }
                logger.error("Calling blockchain to fund  step {}  failed with exception {}",
                        step, e.getMessage());
                return new TransactionHashResponse(transactionHash, returnCode, returnMessage);
            }

            transactionHash = remove0xPrefix(transactionHash);

            logger.info("FileTransferController.addFunds calling set transfer funding step completed " + fileTransferUuid);
            fileTransferMapper.setTransferFundingStepCompleted(fileTransferUuid, walletAddress, step, transactionHash);

        } else {
            logger.error("Canot start adding funds for step: {} , returnCode:  {}, returnMessage:  {}  ", step, returnCode, returnMessage);
        }

        return new TransactionHashResponse(transactionHash, returnCode, returnMessage);
    }

    private String remove0xPrefix(String oxPrefixedString) {
        String prefix = oxPrefixedString.substring(0, 2);
        String unPrefixedString = "";
        if (!StringUtils.isBlank(oxPrefixedString) && prefix.equals("0x")) {
            unPrefixedString = oxPrefixedString.substring(2);
        }
        return unPrefixedString;
    }

    @PostMapping(value = "/set_transfer_step", produces = { "application/JSON" })
    public ReturnCodeMessageResponse setTransferStep(
            @RequestParam(value = "fileTransferUuid") final UUID fileTransferUuid,
            @RequestParam(value = "transferStep") final String transferStep,
            @RequestParam(value = "transferStepStatus") final String transferStepStatus
            ) throws Exception {
        logger.info("FileTransferController.setTransferStep called for file_transfer_uuid " + fileTransferUuid);

        return fileTransferMapper.setTransferStep(fileTransferUuid, transferStep,
                transferStepStatus);

    }

    private BlockChainGatewayService getBlockChainGateWayService() {
        BlockChainGatewayServiceTypeEnum blockchainGatewayServiceType = BlockChainGatewayServiceTypeEnum
                .fromValue(blockchainGatewayType);
        return blockChainGatewayServiceFactory.createGatewayService(blockchainGatewayServiceType);
    }
}
