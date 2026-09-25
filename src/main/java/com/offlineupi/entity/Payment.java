package com.offlineupi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Payment {

    public Payment() {
    }

    public Payment(
            String senderId,
            String receiverId,
            BigDecimal amount) {
        this.transactionId = java.util.UUID.randomUUID().toString();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.nonce = java.util.UUID.randomUUID().toString();
        this.status = PaymentStatus.CREATED;
    }

    @Id
    private String transactionId;

    private String senderId;

    private String receiverId;

    private BigDecimal amount;

    private LocalDateTime timestamp;

    private String nonce;

    private String packetId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public String getTransactionId() {
        return transactionId;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

}