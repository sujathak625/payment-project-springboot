package com.cgi.fsdc.utilities;

import com.cgi.fsdc.exception.EncryptionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Base64;

@Component
public class SecurityUtils {

    @Value("${encryption.secret.key}")
    private String secretKeyBase64;

    private static SecretKey secretKey;

    @PostConstruct
    private void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyBase64);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (GeneralSecurityException e) {
            throw new EncryptionException("Error during encryption", e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (GeneralSecurityException e) {
            throw new EncryptionException("Error during decryption", e);
        }
    }
}
