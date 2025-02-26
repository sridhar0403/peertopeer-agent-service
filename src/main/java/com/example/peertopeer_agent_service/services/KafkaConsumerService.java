package com.example.peertopeer_agent_service.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "agent-messages", groupId = "agent-group")
    public void listen(String message) {
        System.out.println("Received message in Spring: " + message);
    }
}
