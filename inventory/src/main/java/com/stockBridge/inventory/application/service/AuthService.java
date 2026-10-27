package com.stockBridge.inventory.application.service;

import com.stockBridge.inventory.domain.exception.DuplicateResourceException;
import com.stockBridge.inventory.domain.exception.UnauthorizedAccessException;
import com.stockBridge.inventory.domain.model.Role;
import com.stockBridge.inventory.domain.model.User;
import com.stockBridge.inventory.domain.port.in.AuthUseCase;
import com.stockBridge.inventory.domain.port.out.JwtTokenPort;
import com.stockBridge.inventory.domain.port.out.PasswordEncoderPort;
import com.stockBridge.inventory.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

// Implementacion de casos de uso de autenticacion y registro
@Service
@Transactional
public class AuthService implements AuthUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtTokenPort jwtTokenPort;

    public AuthService(UserRepositoryPort userRepository,
                       PasswordEncoderPort passwordEncoder,
                       JwtTokenPort jwtTokenPort) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenPort = jwtTokenPort;
    }

    @Override
    public User register(String username, String password, Role role) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("El nombre de usuario ya esta en uso: " + username);
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("La contrasenia debe tener minimo 6 caracteres");
        }

        Role assignedRole = role != null ? role : Role.ROLE_EMPLEADO;
        String encodedPassword = passwordEncoder.encode(password);

        User newUser = new User(null, username, encodedPassword, assignedRole);
        return userRepository.save(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedAccessException("Credenciales invalidas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedAccessException("Credenciales invalidas");
        }

        return jwtTokenPort.generateToken(user.getUsername(), user.getRole().name(), Map.of("userId", user.getId()));
    }
}
