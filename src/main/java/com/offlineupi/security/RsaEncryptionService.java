package com.offlineupi.security;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Service
public class RsaEncryptionService {

    private static final String TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    public String encrypt(
            String plainText,
            PublicKey publicKey) throws Exception {

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        cipher.init(
                Cipher.ENCRYPT_MODE,
                publicKey);

        byte[] encryptedBytes = cipher.doFinal(
                plainText.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder()
                .encodeToString(encryptedBytes);
    }

    public String decrypt(
            String encryptedText,
            PrivateKey privateKey) throws Exception {

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        cipher.init(
                Cipher.DECRYPT_MODE,
                privateKey);

        byte[] encryptedBytes = Base64.getDecoder()
                .decode(encryptedText);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(
                decryptedBytes,
                StandardCharsets.UTF_8);
    }
}