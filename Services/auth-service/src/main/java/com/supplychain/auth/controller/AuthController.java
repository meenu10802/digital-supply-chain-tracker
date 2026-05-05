package com.supplychain.auth.controller;

import com.supplychain.auth.dto.*;
import com.supplychain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "Authentication endpoints")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @Valid @RequestBody RegisterRequestDto dto) {
        return ResponseEntity.status(201)
                .body(authService.register(dto));
    }

    @Operation(summary = "Login and receive JWT")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @Operation(summary = "Refresh access token")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(
            @Valid @RequestBody RefreshTokenRequestDto dto) {
        return ResponseEntity.ok(authService.refresh(dto));
    }

    @Operation(summary = "Logout (revoke token)")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String header) {
        authService.logout(header.substring(7));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get current user info")
    @GetMapping("/me")
    public ResponseEntity<AuthResponseDto> me(
            @RequestHeader("Authorization") String header) {
        return ResponseEntity.ok(
                authService.getMe(header.substring(7)));
    }
    @Operation(summary = "Assign role (ADMIN only)")
    @PostMapping("/admin/assign-role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> assignRole(
            @Valid @RequestBody RoleAssignmentDto dto) {
        authService.assignRole(dto);
        return ResponseEntity.ok().build();
    }
}