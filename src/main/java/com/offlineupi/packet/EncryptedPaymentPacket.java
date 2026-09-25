package com.offlineupi.packet;

public class EncryptedPaymentPacket {

    private String packetId;
    private String transactionId;
    private String encryptedPayload;

    public EncryptedPaymentPacket(
            String packetId,
            String transactionId,
            String encryptedPayload) {
        this.packetId = packetId;
        this.transactionId = transactionId;
        this.encryptedPayload = encryptedPayload;
    }

    public String getPacketId() {
        return packetId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getEncryptedPayload() {
        return encryptedPayload;
    }
}