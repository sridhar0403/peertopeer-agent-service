package com.example.peertopeer_agent_service.utils;

import java.security.Key;

// Visible only within the same package
class JwtUtilTestUtils {
    static Key getSameKeyAsUtil() {
        // Because SECRET_KEY is private, we either
        // 1) Make SECRET_KEY package-private, or
        // 2) Provide a package-private getter in JwtUtil
        // For simplicity, let's just do #2 in JwtUtil.
        return JwtUtil.getSecretKey();
    }
}
