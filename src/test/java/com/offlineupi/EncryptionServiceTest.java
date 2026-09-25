package com.offlineupi;

import com.offlineupi.security.EncryptionService;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncryptionServiceTest {

        @Test
        void encryptionAndDecryptionShouldWork() throws Exception {

                EncryptionService encryptionService = new EncryptionService();

                SecretKey key = encryptionService.generateKey();

                String originalMessage = "Suprit pays Santy 5000";

                String encryptedMessage = encryptionService.encrypt(
                                originalMessage,
                                key);

                String decryptedMessage = encryptionService.decrypt(
                                encryptedMessage,
                                key);

                assertEquals(
                                originalMessage,
                                decryptedMessage);
        }
}