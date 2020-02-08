package com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo;

import java.util.UUID;

public class FileTransferOrderLineItemUsageVO {
    private UUID uuid;
    private long order_line_item_id;
    private long sender_id;
    private String sender_login_name;
    private long receiver_id;
    private String receiver_login_name;
    private short credit_used;
    private String status;
    private String transfer_dt;
    private String smart_contract_address;
    private String is_active;
    private int package_id;
    private String package_name;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


    public long getOrderLineItemId() {
        return order_line_item_id;
    }

    public void setOrderLineItemId(long orderLineItemId) {
        this.order_line_item_id = orderLineItemId;
    }

    public long getSenderId() {
        return sender_id;
    }

    public void setSenderId(long senderId) {
        this.sender_id = senderId;
    }

    public String getSenderLoginName() {
        return sender_login_name;
    }

    public void setSenderLoginName(String senderLoginName) {
        this.sender_login_name = senderLoginName;
    }

    public long getReceiverId() {
        return receiver_id;
    }

    public void setReceiverId(long receiverId) {
        this.receiver_id = receiverId;
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

    public String getIsActive() {
        return is_active;
    }

    public void setIsActive(String isActive) {
        this.is_active = isActive;
    }

    public int getPackageId() {
        return package_id;
    }

    public void setPackageId(int packageId) {
        this.package_id = packageId;
    }

    public String getPackageName() {
        return package_name;
    }

    public void setPackageName(String packageName) {
        this.package_name = packageName;
    }

}
