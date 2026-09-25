package com.offlineupi;

import com.offlineupi.packet.HybridEncryptedPaymentPacket;
import com.offlineupi.packet.PaymentPacket;
import com.offlineupi.security.EncryptionService;
import com.offlineupi.security.HybridEncryptionService;
import com.offlineupi.security.RsaEncryptionService;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HybridEncryptionServiceTest {

    @Test
    void shouldEncryptAndDecryptPaymentPacketSuccessfully() throws Exception {

        // Generate receiver RSA key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(2048);

        KeyPair receiverKeyPair = keyPairGenerator.generateKeyPair();

        // Create required services
        EncryptionService encryptionService = new EncryptionService();

        RsaEncryptionService rsaEncryptionService = new RsaEncryptionService();

        HybridEncryptionService hybridEncryptionService = new HybridEncryptionService(
                encryptionService,
                rsaEncryptionService);

        // Create original payment packet
        PaymentPacket originalPacket = new PaymentPacket(
                "packet-123",
                "transaction-123",
                "suprit",
                "priya",
                new java.math.BigDecimal("500"),
                LocalDateTime.now(),
                "nonce-123",
                5);

        // Encrypt using hybrid encryption
        HybridEncryptedPaymentPacket encryptedPacket = hybridEncryptionService.encryptPayment(
                originalPacket,
                receiverKeyPair.getPublic());

        // Make sure encrypted data exists
        assertNotNull(encryptedPacket);
        assertNotNull(encryptedPacket.getEncryptedPayload());
        assertNotNull(encryptedPacket.getEncryptedAesKey());

        // Decrypt at receiver
        PaymentPacket decryptedPacket = hybridEncryptionService.decryptPayment(
                encryptedPacket,
                receiverKeyPair.getPrivate());

        // Verify original packet was recovered
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