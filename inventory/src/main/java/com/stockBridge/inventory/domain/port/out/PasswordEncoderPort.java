package com.stockBridge.inventory.domain.port.out;

// Puerto de salida para codificar contraseñas sin acoplar el dominio a Spring Security
public interface PasswordEncoderPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
