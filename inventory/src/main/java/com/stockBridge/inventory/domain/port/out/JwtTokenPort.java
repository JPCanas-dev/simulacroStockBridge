package com.stockBridge.inventory.domain.port.out;

import java.util.Map;

// Puerto de salida para generacion y validacion de tokens JWT
public interface JwtTokenPort {
    String generateToken(String username, String role, Map<String, Object> extraClaims);
    String extractUsername(String token);
    boolean isTokenValid(String token, String username);
}
