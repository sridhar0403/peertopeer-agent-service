package com.example.peertopeer_agent_service.utils;

import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void generateToken_givenAgentId_shouldReturnNonEmptyToken() {
        // Arrange
        String agentId = "agent123";

        // Act
        String token = JwtUtil.generateToken(agentId);

        // Assert
        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");
    }

    @Test
    void validateToken_givenValidToken_shouldReturnTrue() {
        // Arrange
        String agentId = "agent123";
        String token = JwtUtil.generateToken(agentId);

        // Act
        boolean isValid = JwtUtil.validateToken(token);

        // Assert
        assertTrue(isValid, "A freshly generated token should be valid");
    }

    @Test
    void validateToken_givenExpiredToken_shouldReturnFalse() throws InterruptedException {
        // We need an expired token. One approach:
        // 1. Modify JwtUtil temporarily to set a 1ms expiration,
        //    OR
        // 2. Generate a token with a known short validity, then sleep past it,
        //    OR
        // 3. Generate a token with a past expiration date directly here.
        //
        // For simplicity, let's craft a token with an expired date using the direct builder.
        // We'll replicate the logic from JwtUtil but set the expiration in the past.

        long now = System.currentTimeMillis();
        String expiredToken = io.jsonwebtoken.Jwts.builder()
                .setSubject("agent123")
                .setIssuedAt(new java.util.Date(now - 10000)) // 10s in the past
                .setExpiration(new java.util.Date(now - 5000)) // 5s in the past
                .signWith(JwtUtilTestUtils.getSameKeyAsUtil()) // We'll get the same key
                .compact();

        // Act
        boolean isValid = JwtUtil.validateToken(expiredToken);

        // Assert
        assertFalse(isValid, "Token that is already expired should be invalid");
    }

    @Test
    void validateToken_givenTamperedToken_shouldReturnFalse() {
        // Arrange
        String token = JwtUtil.generateToken("agent123");

        // Tamper with the token by changing a character
        // This typically invalidates the signature
        String tamperedToken = token.substring(0, token.length() - 2) + "ab";

        // Act
        boolean isValid = JwtUtil.validateToken(tamperedToken);

        // Assert
        assertFalse(isValid, "A tampered token should be invalid");
    }

}
