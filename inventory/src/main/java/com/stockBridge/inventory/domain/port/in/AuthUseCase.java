package com.stockBridge.inventory.domain.port.in;

import com.stockBridge.inventory.domain.model.Role;
import com.stockBridge.inventory.domain.model.User;

// Casos de uso de autenticacion y registro
public interface AuthUseCase {
    User register(String username, String password, Role role);
    String login(String username, String password);
}
