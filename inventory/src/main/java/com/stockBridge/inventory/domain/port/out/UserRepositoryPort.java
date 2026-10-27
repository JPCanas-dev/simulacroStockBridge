package com.stockBridge.inventory.domain.port.out;

import com.stockBridge.inventory.domain.model.User;
import java.util.Optional;

// Puerto de salida para persistencia de usuarios
public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
