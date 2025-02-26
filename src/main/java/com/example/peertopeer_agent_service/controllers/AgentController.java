package com.example.peertopeer_agent_service.controllers;

import com.example.peertopeer_agent_service.dto.MessageRequest;
import com.example.peertopeer_agent_service.services.KafkaProducerService;
import com.example.peertopeer_agent_service.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.example.peertopeer_agent_service.dto.MessageRequest;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AgentController {

    @GetMapping("/ping")
    public String ping() {
        return "Peer-to-Peer Agent Service is running!";
    }

    @Autowired
    private KafkaProducerService producerService;

    @PostMapping(value = "/send", consumes = "application/json")
    public String sendMessage(@RequestBody MessageRequest request) {
        producerService.sendMessage(request.getCompanyId(), request.getMessage());
        return "Encrypted message sent to " + request.getCompanyId() + ": " + request.getMessage();
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String agentId) {
        return JwtUtil.generateToken(agentId);
    }
}