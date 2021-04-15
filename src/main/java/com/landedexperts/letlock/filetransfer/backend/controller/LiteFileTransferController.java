/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.LITE_FILE_TRANSFER_PRODUCT_NAME;
import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.USER_ID;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.LiteFileTransferMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.OrderMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.BooleanResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.FileUploadResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.LiteFileTransferSessionResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.LiteFileTransferSessionsResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.OrdersFileTransfersCountsResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.OrdersFileTransfersCountsVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.UuidResponse;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.BooleanPathnameVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.LiteFileTransferInfoVO;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.LiteFileTransferRecordVO;

@RestController
@RequestMapping(value = "/lite_file_transfer", produces = { "application/JSON" })
public class LiteFileTransferController {
    @Autowired
    LiteFileTransferMapper liteFileTransferMapper;
    
    @Autowired
    OrderMapper orderMapper;
    
    @Autowired
    FileController fileController;

    private final Logger logger = LoggerFactory.getLogger(FileTransferController.class);

    @PostMapping()
    public UuidResponse startFileTransferSession(
            @RequestParam final String receiverLoginName,
            final HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        logger.info("FileTransferController.startFileTransferSession called for userId " + userId);

        return liteFileTransferMapper.createFileTransferSession(userId, receiverLoginName);
    }
    
    @GetMapping()
    public LiteFileTransferSessionResponse getFileTransferStatus(
            @RequestParam final UUID fileTransferUuid,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        logger.info("LiteFileTransferController.getFileTransferStatus called for userId " + userId);

        LiteFileTransferInfoVO answer = liteFileTransferMapper.getFileTransferInfo(userId, fileTransferUuid);
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();
        
        logger.info("returnCode: " + returnCode + "  record " + answer.getFileTransferInfoRecord());

        return new LiteFileTransferSessionResponse(answer.getFileTransferInfoRecord(), returnCode, returnMessage);
    }
    
    @GetMapping(value = "/list")
    public LiteFileTransferSessionsResponse getFileTransferSessionsForUser(
            HttpServletRequest request) throws Exception {
 
        long userId = (long) request.getAttribute(USER_ID);
        LiteFileTransferRecordVO[] value = liteFileTransferMapper.getFileTransferSessionsForUser(userId);

        return new LiteFileTransferSessionsResponse(value, "SUCCESS", "");
    }
    
    @GetMapping(value = "/usage_counts")
    public OrdersFileTransfersCountsResponse getFileTransferUsageCount(
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        logger.info("LiteFileTransferController.getFileTransferUsageCount called for userId " + userId);
    
        OrdersFileTransfersCountsVO fileTransferCounts = orderMapper.getOrdersFileTransferUsageCounts(
                userId, -1, LITE_FILE_TRANSFER_PRODUCT_NAME);
        if (null == fileTransferCounts) {
            fileTransferCounts = new OrdersFileTransfersCountsVO(); 
        }
        return new OrdersFileTransfersCountsResponse(fileTransferCounts);
    }
    
    @PutMapping(value = "/current_step")
    public ReturnCodeMessageResponse setTransferStep(
            @RequestParam final UUID fileTransferUuid,
            @RequestParam final String transferStep,
            @RequestParam final String transferStepStatus
            ) throws Exception {
        logger.info("LiteFileTransferController.setTransferStep called for file_transfer_uuid " + fileTransferUuid);

        return liteFileTransferMapper.setTransferStep(fileTransferUuid, transferStep, transferStepStatus);
    }

    @PutMapping(value = "/inactive")
    public ReturnCodeMessageResponse setFileTransferInactive(
            @RequestParam final UUID fileTransferUuid,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        logger.info("LiteFileTransferController.setFileTransferInactive called for fileTransferUuid " + fileTransferUuid);
        
        return liteFileTransferMapper.setFileTransferInactive(userId, fileTransferUuid);
    }

    @PutMapping(value = "/accepted")
    public ReturnCodeMessageResponse setFileTransferReceiverAccept(
            @RequestParam final UUID fileTransferUuid,
            @RequestParam final String publicKey,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        logger.info("LiteFileTransferController.setFileTransferReceiverAccept called for fileTransferUuid: " + fileTransferUuid);
        return liteFileTransferMapper.setFileTransferReceiverAccept(userId, fileTransferUuid, publicKey);
    }
    
    @GetMapping(value = "/public_key")
    public LiteFileTransferSessionResponse getFileTransferPublicKey(
            @RequestParam final UUID fileTransferUuid,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        logger.info("LiteFileTransferController.getFileTransferPublicKey called for fileTransferUuid " + fileTransferUuid);
        
        LiteFileTransferInfoVO answer = liteFileTransferMapper.getFileTransferPublicKey(userId, fileTransferUuid);

        return new LiteFileTransferSessionResponse(answer.getFileTransferInfoRecord(),
                answer.getReturnCode(), answer.getReturnMessage());
    }

    @PostMapping(value = "/document_info")
    public ReturnCodeMessageResponse setFileTransferDocumentInfo(
            @RequestParam final UUID fileTransferUuid,
            @RequestParam final String documentHash,
            @RequestParam final String encryptedDocumentHash,
            @RequestParam final String encryptedDocumentKey,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        logger.info("LiteFileTransferController.setFileTransferDocumentInfo called for fileTransferUuid: " + fileTransferUuid);

        return liteFileTransferMapper.setFileTransferDocumentInfo(
                userId, fileTransferUuid, documentHash, encryptedDocumentHash, encryptedDocumentKey);
    }
    
    @PostMapping(value = "/file")
    public BooleanResponse uploadFile(
            @RequestParam final UUID fileTransferUuid,
            @RequestParam final MultipartFile file,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        logger.info("LiteFileTransferController.upload_file called for fileTransferUuid " + fileTransferUuid);

        FileUploadResponse response = fileController.saveFileOnRemote(file);
        String returnCode = response.getReturnCode();
        String returnMessage = response.getReturnMessage();
        if (returnCode.equals("SUCCESS")) {
            ReturnCodeMessageResponse answer = liteFileTransferMapper.insertFileUploadRecord(
                    userId, fileTransferUuid, response.getRemotePathName(), response.getExpires());
            returnCode = answer.getReturnCode();
            returnMessage = answer.getReturnMessage();
        }

        logger.info("FileController.uploadFile returning response with result " + returnCode.equals("SUCCESS"));

        return new BooleanResponse(returnCode.equals("SUCCESS"), returnCode, returnMessage);
    }
    
    @GetMapping(value = "/document_info")
    public LiteFileTransferSessionResponse getFileTransferDocumentInfo(
            @RequestParam final UUID fileTransferUuid,
            HttpServletRequest request) throws Exception {
        long userId = (long) request.getAttribute(USER_ID);
        logger.info("LiteFileTransferController.getFileTransferDocumentInfo called for fileTransferUuid " + fileTransferUuid);
        
        LiteFileTransferInfoVO answer = liteFileTransferMapper.getFileTransferDocumentInfo(userId, fileTransferUuid);
        String returnCode = answer.getReturnCode();
        String returnMessage = answer.getReturnMessage();

        logger.info("returnCode: " + returnCode + "  record " + answer.getFileTransferInfoRecord());

        return new LiteFileTransferSessionResponse(answer.getFileTransferInfoRecord(), returnCode, returnMessage);
    }

    @GetMapping(value = "/download_file_status")
    public BooleanResponse canDownloadFile(
            @RequestParam final UUID fileTransferUuid,
            HttpServletRequest request) throws Exception {
        logger.info("LiteFileTransferController.canDownloadFile called for fileTransferUuid " + fileTransferUuid);

        long userId = (long) request.getAttribute(USER_ID);
        BooleanPathnameVO check = liteFileTransferMapper.canDownloadFile(userId, fileTransferUuid);

        boolean result = check.getValue();
        String returnCode = check.getReturnCode();
        String returnMessage = check.getReturnMessage();

        logger.info("LiteFileTransferController.canDownloadFile response " + result);
        return new BooleanResponse(result, returnCode, returnMessage);
    }
    
    @PostMapping(value = "/download_file")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam final UUID fileTransferUuid,
            HttpServletRequest request) throws Exception {

        long userId = (long) request.getAttribute(USER_ID);
        logger.info("LiteFileTransferController.downloadFile called for fileTransferUuid " + fileTransferUuid);
        
        BooleanPathnameVO check = liteFileTransferMapper.canDownloadFile(userId, fileTransferUuid);
        return fileController.getFileFromRemote(check);
    }
    
}
