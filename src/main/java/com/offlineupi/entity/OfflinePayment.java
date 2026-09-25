package com.offlineupi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class OfflinePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String senderId;
    private String receiverId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String nonce;
    private int ttl;
    private String status;

    public OfflinePayment() {
    }

    public OfflinePayment(
            String transactionId,
            String senderId,
            String receiverId,
            BigDecimal amount,
            LocalDateTime createdAt,
            String nonce,
            int ttl
    ) {
        this.transactionId = transactionId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.nonce = nonce;
        this.ttl = ttl;
        this.status = "QUEUED";
    }

    public Long getId() {
        return id;
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

    public String getStatus() {
        return status;
    }

    public void decrementTtl() {
        if (ttl > 0) {
            ttl--;
        }
    }
}