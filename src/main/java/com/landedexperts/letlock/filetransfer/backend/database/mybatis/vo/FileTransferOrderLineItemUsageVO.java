/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import java.util.UUID;

public class FileTransferOrderLineItemUsageVO {
    private UUID uuid;
    private long order_id;
    private long order_line_item_id;
    private String sender_login_name;
    private String receiver_login_name;
    private short credit_used;
    private String status;
    private String transfer_dt;
    private String smart_contract_address;
    private String package_name;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    
    public long getOrderId() {
        return order_id;
    }

    public void setOrderIdd(long order_id) {
        this.order_id = order_id;
    }


    public long getOrderLineItemId() {
        return order_line_item_id;
    }

    public void setOrderLineItemId(long orderLineItemId) {
        this.order_line_item_id = orderLineItemId;
    }
    public String getSenderLoginName() {
        return sender_login_name;
    }

    public void setSenderLoginName(String senderLoginName) {
        this.sender_login_name = senderLoginName;
    }

    public String getReceiverLoginName() {
        return receiver_login_name;
    }

    public void setReceiverLoginName(String receiverLoginName) {
        this.receiver_login_name = receiverLoginName;
    }

    public short getCreditUsed() {
        return credit_used;
    }

    public void setCreditUsed(short creditUsed) {
        this.credit_used = creditUsed;
    }

    public String getfTStatus() {
        return status;
    }

    public void setfTStatus(String fTStatus) {
        this.status = fTStatus;
    }

    public String getFtDate() {
        return transfer_dt;
    }

    public void setFtDate(String ftDate) {
        this.transfer_dt = ftDate;
    }

    public String getContractAddress() {
        return smart_contract_address;
    }

    public void setContractAddress(String contractAddress) {
        this.smart_contract_address = contractAddress;
    }

    public String getPackageName() {
        return package_name;
    }

    public void setPackageName(String packageName) {
        this.package_name = packageName;
    }

}
