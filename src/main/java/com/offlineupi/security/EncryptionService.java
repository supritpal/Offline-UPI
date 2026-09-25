package com.offlineupi.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.offlineupi.packet.EncryptedPaymentPacket;
import com.offlineupi.packet.PaymentPacket;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {

        private static final String AES = "AES";
        private static final String AES_GCM = "AES/GCM/NoPadding";

        private static final int KEY_SIZE = 256;
        private static final int IV_SIZE = 12;
        private static final int TAG_LENGTH = 128;

        private final ObjectMapper objectMapper = new ObjectMapper()
                        .findAndRegisterModules();

        public SecretKey generateKey() throws Exception {

                KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);

                keyGenerator.init(KEY_SIZE);

                return keyGenerator.generateKey();
        }

        public String encrypt(
                        String plainText,
                        SecretKey secretKey) throws Exception {

                // Generate a NEW IV for every encryption
                byte[] iv = new byte[IV_SIZE];

                SecureRandom secureRandom = new SecureRandom();
                secureRandom.nextBytes(iv);

                Cipher cipher = Cipher.getInstance(AES_GCM);

                GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);

                cipher.init(
                                Cipher.ENCRYPT_MODE,
                                secretKey,
                                gcmSpec);

                byte[] encrypted = cipher.doFinal(
                                plainText.getBytes(StandardCharsets.UTF_8));

                // Store IV + ciphertext together
                byte[] result = new byte[iv.length + encrypted.length];

                System.arraycopy(
                                iv,
                                0,
                                result,
                                0,
                                iv.length);

                System.arraycopy(
                                encrypted,
                                0,
                                result,
                                iv.length,
                                encrypted.length);

                return Base64.getEncoder()
                                .encodeToString(result);
        }

        public String decrypt(
                        String encryptedText,
                        SecretKey secretKey) throws Exception {

                byte[] combined = Base64.getDecoder()
                                .decode(encryptedText);

                // Extract IV
                byte[] iv = new byte[IV_SIZE];

                System.arraycopy(
                                combined,
                                0,
                                iv,
                                0,
                                IV_SIZE);

                // Extract ciphertext
                byte[] encrypted = new byte[combined.length - IV_SIZE];

                System.arraycopy(
                                combined,
                                IV_SIZE,
                                encrypted,
                                0,
                                encrypted.length);

                Cipher cipher = Cipher.getInstance(AES_GCM);

                GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);

                cipher.init(
                                Cipher.DECRYPT_MODE,
                                secretKey,
                                gcmSpec);

                byte[] decrypted = cipher.doFinal(encrypted);

                return new String(
                                decrypted,
                                StandardCharsets.UTF_8);
        }

        // -----------------------------------------
        // PAYMENT PACKET ENCRYPTION
        // -----------------------------------------

        public EncryptedPaymentPacket encryptPaymentPacket(
                        PaymentPacket packet,
                        SecretKey secretKey) throws Exception {

                String json = objectMapper.writeValueAsString(packet);

                String encryptedPayload = encrypt(json, secretKey);

                return new EncryptedPaymentPacket(
                                packet.getPacketId(),
                                packet.getTransactionId(),
                                encryptedPayload);
        }

        // -----------------------------------------
        // PAYMENT PACKET DECRYPTION
        // -----------------------------------------

        public PaymentPacket decryptPaymentPacket(
                        EncryptedPaymentPacket encryptedPacket,
                        SecretKey secretKey) throws Exception {

                String json = decrypt(
                                encryptedPacket.getEncryptedPayload(),
                                secretKey);

                return objectMapper.readValue(
                                json,
                                PaymentPacket.class);
        }
}