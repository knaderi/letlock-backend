package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayService;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceFactory;
import com.landedexperts.letlock.filetransfer.backend.blockchain.gateway.BlockChainGatewayServiceTypeEnum;
import com.landedexperts.letlock.filetransfer.backend.database.mapper.FileTransferMapper;
import com.landedexperts.letlock.filetransfer.backend.database.vo.BooleanVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.FileTransferInfoRecord;
import com.landedexperts.letlock.filetransfer.backend.database.vo.FileTransferInfoVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.GochainAddressVO;
import com.landedexperts.letlock.filetransfer.backend.database.vo.UuidNameDateVO;
import com.landedexperts.letlock.filetransfer.backend.response.ConsumeResponse;
import com.landedexperts.letlock.filetransfer.backend.response.ErrorCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.response.FileTransferSessionResponse;
import com.landedexperts.letlock.filetransfer.backend.response.FileTransferSessionsResponse;
import com.landedexperts.letlock.filetransfer.backend.response.TransactionHashResponse;
import com.landedexperts.letlock.filetransfer.backend.response.UuidNameDate;
import com.landedexperts.letlock.filetransfer.backend.response.UuidNameDateArrayResponse;
import com.landedexperts.letlock.filetransfer.backend.response.UuidResponse;
import com.landedexperts.letlock.filetransfer.backend.session.SessionManager;

@RestController
public class FileTransferController {
    @Autowired
    FileTransferMapper fileTransferMapper;

    @Value("${blockchain.gateway.type}")
    private String blockchainGatewayType;

    @Autowired
    BlockChainGatewayServiceFactory blockChainGatewayServiceFactory;

    private final Logger logger = LoggerFactory.getLogger(FileTransferController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/start_file_transfer_session", produces = {
            "application/JSON" })
    public ConsumeResponse startFileTransferSession(@RequestParam(value = "token") final String token,
            @RequestParam(value = "wallet_address") final String walletAddress,
            @RequestParam(value = "receiver_login_name") final String receiverLoginName) throws Exception {
        logger.info("FileTransferController.startFileTransferSession called for token " + token);
        String fileTransferUuid = null;
        String walletAddressUuid = null;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        Integer userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            String walletAddressTrimmed = walletAddress.substring(0, 2).equals("0x") ? walletAddress.substring(2)
                    : walletAddress;

//            FileTransferSessionVO answer = fileTransferMapper.insertFileTransferSessionRecord(userId,
//                    walletAddressTrimmed, receiverLoginName);
            
            FileTransferInfoVO answer = fileTransferMapper.insertFileTransferSessionRecord(userId,
                    walletAddressTrimmed, receiverLoginName);

            fileTransferUuid = answer.getFileTransferUuid();
            walletAddressUuid = answer.getReceiverWalletAddressUuid();
            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();
        }

        return new ConsumeResponse(fileTransferUuid, walletAddressUuid, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/is_file_transfer_waiting_receiver_address", produces = {
            "application/JSON" })
    public UuidNameDateArrayResponse isFileTransferWaitingForReceiverAddress(
            @RequestParam(value = "token") final String token) {
        logger.info("FileTransferController.isFileTransferWaitingForReceiverAddress called for token " + token);
        UuidNameDate[] value = null;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        Integer userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            UuidNameDateVO[] answer = fileTransferMapper.retrieveSessionWaitingForReceiverAddress(userId);

            value = new UuidNameDate[answer.length];
            for (int i = 0; i < answer.length; i++) {
                value[i] = new UuidNameDate(UUID.fromString(answer[i].getUuid()), answer[i].getName(),
                        answer[i].getCreate());
            }
            errorCode = "NO_ERROR";
            errorMessage = "";
        }

        return new UuidNameDateArrayResponse(value, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/set_file_transfer_active", produces = {
            "application/JSON" })
    public ErrorCodeMessageResponse setFileTransferActive(@RequestParam(value = "token") final String token,
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid) throws Exception {
        logger.info("FileTransferController.setFileTransferAsActive called for token " + token);
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        int userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
        	ErrorCodeMessageResponse answer = fileTransferMapper.setFileTransferAsActive(userId, fileTransferUuid);

            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();
        }

        return new ErrorCodeMessageResponse(errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/set_file_transfer_inactive", produces = {
            "application/JSON" })
    public ErrorCodeMessageResponse setFileTransferInactive(@RequestParam(value = "token") final String token,
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid) throws Exception {
        logger.info("FileTransferController.setFileTransferInactive called for token " + token);
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        int userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
        	ErrorCodeMessageResponse answer = fileTransferMapper.setFileTransferInactive(userId, fileTransferUuid);

            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();
        }

        return new ErrorCodeMessageResponse(errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get_file_transfer_sessions_for_user", produces = {
            "application/JSON" })
    public FileTransferSessionsResponse getFileTransferSessionsForUser(
            @RequestParam(value = "token") final String token) throws Exception {
        logger.info("FileTransferController.getFileTransferSessionsForUser called for token " + token + "\n");

        FileTransferInfoRecord[] value = null;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        int userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
        	value = fileTransferMapper.getFileTransferSessionsForUser(userId);
            errorCode = "NO_ERROR";
            errorMessage = "";
        }

        return new FileTransferSessionsResponse(value, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get_file_transfer_status", produces = { "application/JSON" })
    public FileTransferSessionResponse getFileTransferStatus(@RequestParam(value = "token") final String token,
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid) throws Exception {
        logger.info("FileTransferController.getFileTransferStatus called for token " + token);
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";
        FileTransferInfoVO answer = new FileTransferInfoVO();

        Integer userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            answer = fileTransferMapper.getUserFileTransferInfo(userId, fileTransferUuid);
            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();
        }

        return new FileTransferSessionResponse(answer.getFileTransferInfoRecord(), errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/set_file_transfer_receiver_address", produces = {
            "application/JSON" })
    public UuidResponse setFileTransferReceiverAddress(@RequestParam(value = "token") final String token,
            @RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid,
            @RequestParam(value = "wallet_address") final String walletAddress) throws Exception {
        logger.info("FileTransferController.setFileTransferReceiverAddress called for token " + token);
        UUID walletAddressUuid = null;
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";

        int userId = SessionManager.getInstance().getUserId(token);
        if (userId > 0) {
            String walletAddressTrimmed = walletAddress.substring(0, 2).equals("0x") ? walletAddress.substring(2)
                    : walletAddress;

            GochainAddressVO answer = fileTransferMapper.setReceiverAddress(userId, fileTransferUuid,
                    walletAddressTrimmed);

            walletAddressUuid = answer.getGochainAddress();
            errorCode = answer.getErrorCode();
            errorMessage = answer.getErrorMessage();

            if (errorCode.equals("NO_ERROR")) {
                FileTransferInfoVO fileTransferInfo = fileTransferMapper.getUserFileTransferInfo(userId,
                        fileTransferUuid);
                String senderWalletAddress = fileTransferInfo.getSenderWalletAddress();
                String receiverWalletAddress = fileTransferInfo.getReceiverWalletAddress();

                @SuppressWarnings("unused")
                boolean response = getBlockChainGateWayService().deploySmartContract(fileTransferUuid,
                        "0x" + senderWalletAddress, "0x" + receiverWalletAddress);
            }
        }

        return new UuidResponse(walletAddressUuid, errorCode, errorMessage);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get_txn_status", produces = { "application/JSON" })
    public TransactionHashResponse searchTransactionHash(@RequestParam(value = "token") final String token,
            @RequestParam(value = "transactionHash") final String transactionHash) throws Exception {
        logger.info("FileTransferController.searchTransactionHash called for token " + token);
        String errorCode = "TOKEN_INVALID";
        String errorMessage = "Invalid token";
        int userId = SessionManager.getInstance().getUserId(token);
        TransactionHashResponse transactionHashResponse = null;
        if (userId > 0) {
            transactionHashResponse = getBlockChainGateWayService().getTransactionStatus(transactionHash);

        } else {
            transactionHashResponse = new TransactionHashResponse(errorCode, errorMessage);
        }
        return transactionHashResponse;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add_funds", produces = { "application/JSON" })
    public TransactionHashResponse addFunds(@RequestParam(value = "file_transfer_uuid") final UUID fileTransferUuid,
            @RequestParam(value = "signed_transaction_hex") final String signedTransactionHex,
            @RequestParam(value = "step") final String step) throws Exception {
        logger.info("FileTransferController.addFunds called for file_transfer_uuid " + fileTransferUuid);
        String errorCode = "";
        String errorMessage = "";

        String walletAddress = getBlockChainGateWayService().getWalletAddressFromTransaction(fileTransferUuid,
                signedTransactionHex, step);

        String prefix = walletAddress.substring(0, 2);
        if (prefix.equals("0x")) {
            walletAddress = walletAddress.substring(2);
        }

        BooleanVO isAvailable = fileTransferMapper.setFileTransferStepAvailability(fileTransferUuid, walletAddress,
                step);

        errorCode = isAvailable.getErrorCode();
        errorMessage = isAvailable.getErrorMessage();

        String transactionHash = "";
        if (errorCode.equals("NO_ERROR") && isAvailable.getValue()) {
            transactionHash = getBlockChainGateWayService().fund(fileTransferUuid, signedTransactionHex, step);
        }

        return new TransactionHashResponse(transactionHash, errorCode, errorMessage);
    }

    private BlockChainGatewayService getBlockChainGateWayService() {
        BlockChainGatewayServiceTypeEnum blockchainGatewayServiceType = BlockChainGatewayServiceTypeEnum
                .fromValue(blockchainGatewayType);
        return blockChainGatewayServiceFactory.createGatewayService(blockchainGatewayServiceType);
    }
}
