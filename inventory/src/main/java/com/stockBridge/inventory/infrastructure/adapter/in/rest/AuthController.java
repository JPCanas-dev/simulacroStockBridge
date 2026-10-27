package com.stockBridge.inventory.infrastructure.adapter.in.rest;

import com.stockBridge.inventory.domain.model.User;
import com.stockBridge.inventory.domain.port.in.AuthUseCase;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.request.LoginRequest;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.request.RegisterRequest;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response.AuthResponse;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response.UserResponse;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.mapper.RestDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador para autenticacion y registro de usuarios
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticacion", description = "Endpoints para registro y login con JWT")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final RestDtoMapper restMapper;

    public AuthController(AuthUseCase authUseCase, RestDtoMapper restMapper) {
        this.authUseCase = authUseCase;
        this.restMapper = restMapper;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = authUseCase.register(request.username(), request.password(), request.role());
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toUserResponse(user));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion y obtener JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authUseCase.login(request.username(), request.password());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
