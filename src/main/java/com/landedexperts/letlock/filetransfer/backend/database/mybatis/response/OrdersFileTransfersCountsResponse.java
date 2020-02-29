/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.response;

public class OrdersFileTransfersCountsResponse extends ReturnCodeMessageResponse {

    private int availableTransferCounts = 0;
    private int originalTransferCounts = 0;
    
    public OrdersFileTransfersCountsResponse(String returnCode, String returnMessage, int availableTransferCounts, int originalTransferCounts) {
        super(returnCode, returnMessage);
        this.availableTransferCounts = availableTransferCounts;
        this.originalTransferCounts = originalTransferCounts;
    }

    public int getAvailableTransferCounts() {
        return availableTransferCounts;
    }
    public void setAvailableTransferCounts(int availableTransferCounts) {
        this.availableTransferCounts = availableTransferCounts;
    }
    public int getOriginalTransferCounts() {
        return originalTransferCounts;
    }
    public void setOriginalTransferCounts(int originalTransferCounts) {
        this.originalTransferCounts = originalTransferCounts;
    }

    public OrdersFileTransfersCountsResponse() {
        // TODO Auto-generated constructor stub
    }

}
