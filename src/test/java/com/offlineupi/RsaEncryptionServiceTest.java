package com.offlineupi;

import com.offlineupi.security.RsaEncryptionService;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RsaEncryptionServiceTest {

    @Test
    void shouldEncryptAndDecryptSuccessfully() throws Exception {

        // Generate RSA key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(2048);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Create RSA encryption service
        RsaEncryptionService rsaEncryptionService = new RsaEncryptionService();

        // Original message
        String originalMessage = "Offline UPI AES Session Key";

        // Encrypt using public key
        String encryptedMessage = rsaEncryptionService.encrypt(
                originalMessage,
                keyPair.getPublic());

        // Make sure encryption actually changed the data
        assertNotEquals(
                originalMessage,
                encryptedMessage);

        // Decrypt using private key
        String decryptedMessage = rsaEncryptionService.decrypt(
                encryptedMessage,
                keyPair.getPrivate());

        // Original and decrypted data must match
        assertEquals(
                originalMessage,
                decryptedMessage);
    }
}