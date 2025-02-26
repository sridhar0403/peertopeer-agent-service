package com.example.peertopeer_agent_service.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTest {

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @Test
    void encryptThenDecrypt_shouldReturnOriginalMessage() {
        // Arrange
        String originalMessage = "Hello World";
        String secretKey = KafkaProducerService.SECRET_KEY;

        // Act
        String encrypted = kafkaProducerService.encrypt(originalMessage, secretKey);
        String decrypted = kafkaProducerService.decrypt(encrypted, secretKey);

        // Assert
        assertEquals(originalMessage, decrypted);
    }

}
