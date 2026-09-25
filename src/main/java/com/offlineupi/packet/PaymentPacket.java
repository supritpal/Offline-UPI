package com.offlineupi.packet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentPacket {
    public PaymentPacket(
            String transactionId,
            String senderId,
            String receiverId,
            BigDecimal amount,
            LocalDateTime createdAt,
            String nonce) {
        this.packetId = UUID.randomUUID().toString();
        this.transactionId = transactionId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.nonce = nonce;
        this.ttl = 5;
    }

    public PaymentPacket(
            String packetId,
            String transactionId,
            String senderId,
            String receiverId,
            BigDecimal amount,
            LocalDateTime createdAt,
            String nonce,
            int ttl) {
        this.packetId = packetId;
        this.transactionId = transactionId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.nonce = nonce;
        this.ttl = ttl;
    }

    public PaymentPacket() {
    }

    private String packetId;

    private String transactionId;

    private String senderId;

    private String receiverId;

    private BigDecimal amount;

    private LocalDateTime createdAt;

    private String nonce;

    private int ttl;

    public String getPacketId() {
        return packetId;
    }

    public String getTransactionId() {
        return transactionId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getNonce() {
        return nonce;
    }

    public int getTtl() {
        return ttl;
    }

    public void decrementTtl() {
        if (ttl > 0) {
            ttl--;
        }
    }

}