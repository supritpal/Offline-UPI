package com.offlineupi;

import com.offlineupi.packet.EncryptedPaymentPacket;
import com.offlineupi.packet.PaymentPacket;
import com.offlineupi.security.EncryptionService;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentPacketEncryptionTest {

    @Test
    void paymentPacketEncryptionShouldWork() throws Exception {

        EncryptionService encryptionService = new EncryptionService();

        // Generate AES key
        SecretKey key = encryptionService.generateKey();

        // Create original payment packet
        PaymentPacket originalPacket = new PaymentPacket(
                "packet-123",
                "transaction-123",
                "Suprit",
                "Santy",
                new BigDecimal("5000"),
                LocalDateTime.now(),
                "nonce-123",
                5);

        // Encrypt packet
        EncryptedPaymentPacket encryptedPacket = encryptionService.encryptPaymentPacket(
                originalPacket,
                key);

        // Decrypt packet
        PaymentPacket decryptedPacket = encryptionService.decryptPaymentPacket(
                encryptedPacket,
                key);

        // Verify original == decrypted
        assertEquals(
                originalPacket.getPacketId(),
                decryptedPacket.getPacketId());

        assertEquals(
                originalPacket.getTransactionId(),
                decryptedPacket.getTransactionId());

        assertEquals(
                originalPacket.getSenderId(),
                decryptedPacket.getSenderId());

        assertEquals(
                originalPacket.getReceiverId(),
                decryptedPacket.getReceiverId());

        assertEquals(
                originalPacket.getAmount(),
                decryptedPacket.getAmount());

        assertEquals(
                originalPacket.getNonce(),
                decryptedPacket.getNonce());

        assertEquals(
                originalPacket.getTtl(),
                decryptedPacket.getTtl());
    }
}