package com.offlineupi.security;

import com.offlineupi.packet.HybridEncryptedPaymentPacket;
import com.offlineupi.packet.PaymentPacket;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

@Service
public class HybridEncryptionService {

    private final EncryptionService encryptionService;
    private final RsaEncryptionService rsaEncryptionService;

    public HybridEncryptionService(
            EncryptionService encryptionService,
            RsaEncryptionService rsaEncryptionService) {

        this.encryptionService = encryptionService;
        this.rsaEncryptionService = rsaEncryptionService;
    }

    public HybridEncryptedPaymentPacket encryptPayment(
            PaymentPacket packet,
            PublicKey receiverPublicKey) throws Exception {

        // 1. Generate a random AES session key
        SecretKey aesKey = encryptionService.generateKey();

        // 2. Encrypt the payment using AES-256-GCM
        String encryptedPayload = encryptionService.encryptPaymentPacket(
                packet,
                aesKey)
                .getEncryptedPayload();

        // 3. Convert AES key into Base64
        String aesKeyBase64 = java.util.Base64.getEncoder()
                .encodeToString(
                        aesKey.getEncoded());

        // 4. Encrypt the AES key using receiver's RSA public key
        String encryptedAesKey = rsaEncryptionService.encrypt(
                aesKeyBase64,
                receiverPublicKey);

        // 5. Create hybrid encrypted packet
        return new HybridEncryptedPaymentPacket(
                packet.getPacketId(),
                packet.getTransactionId(),
                encryptedPayload,
                encryptedAesKey);
    }

    public PaymentPacket decryptPayment(
            HybridEncryptedPaymentPacket packet,
            PrivateKey receiverPrivateKey) throws Exception {

        // 1. Recover AES key using RSA private key
        String aesKeyBase64 = rsaEncryptionService.decrypt(
                packet.getEncryptedAesKey(),
                receiverPrivateKey);

        // 2. Convert Base64 back into AES key bytes
        byte[] aesKeyBytes = java.util.Base64.getDecoder()
                .decode(aesKeyBase64);

        SecretKey aesKey = new javax.crypto.spec.SecretKeySpec(
                aesKeyBytes,
                "AES");

        // 3. Reconstruct EncryptedPaymentPacket
        com.offlineupi.packet.EncryptedPaymentPacket encryptedPacket = new com.offlineupi.packet.EncryptedPaymentPacket(
                packet.getPacketId(),
                packet.getTransactionId(),
                packet.getEncryptedPayload());

        // 4. Decrypt payment using recovered AES key
        return encryptionService.decryptPaymentPacket(
                encryptedPacket,
                aesKey);
    }
}