package com.example.peertopeer_agent_service.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class KafkaProducerService {
    private static final String TOPIC = "agent-messages";
    // In KafkaProducerService.java
    static final String SECRET_KEY = "12345678901234567890123456789012"; // 32-byte key
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    String getSecretKey() {
        return SECRET_KEY;
    }

    public void sendMessage(String companyId, String message) {
        String topic = companyId + "-agent-messages"; // Results in "companyA-agent-messages"
        String encryptedMessage = encrypt(message, SECRET_KEY);
        kafkaTemplate.send(topic, encryptedMessage);
        System.out.println("Sent encrypted message to " + topic + ": " + encryptedMessage);
    }

    public String encrypt(String message, String secretKey) {
        try {
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // PKCS5Padding
            byte[] ivBytes = new byte[16]; // 16-byte IV (all zeros)
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encryptedBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes); // Base64 encode
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String encryptedBase64, String secretKey) {
        try {
            // Re-create your key
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            // Same transformation: AES/CBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // Use the same zero IV you used for encryption
            byte[] ivBytes = new byte[16]; // all zeros
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            // Initialize cipher in DECRYPT_MODE
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

            // Decode Base64 to get the raw cipher bytes
            byte[] cipherBytes = Base64.getDecoder().decode(encryptedBase64);

            // Decrypt
            byte[] decryptedBytes = cipher.doFinal(cipherBytes);

            // Convert back to a String
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }

}