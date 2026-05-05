package com.supplychain.user.controller;

import com.supplychain.user.dto.*;
import com.supplychain.user.enums.RoleEnum;
import com.supplychain.user.security.GatewayUser;
import com.supplychain.user.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User profile management — ADMIN access")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {

    private final UserProfileService userProfileService;

    // ── Internal: called by other services (e.g., after auth-service registers) ──
    @PostMapping("/internal")
    @Operation(summary = "Create user profile (internal service call)")
    public ResponseEntity<UserProfileResponseDto> createProfile(
            @Valid @RequestBody CreateUserProfileRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userProfileService.createProfile(dto));
    }

    // ── Get user by userId — used by Feign clients ─────────────────────────────
    @GetMapping("/internal/by-user-id/{userId}")
    @Operation(summary = "Get profile by userId (internal Feign endpoint)")
    public ResponseEntity<UserProfileResponseDto> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userProfileService.getByUserId(userId));
    }

    // ── List all users (paginated) ─────────────────────────────────────────────
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all users (paginated)")
    public ResponseEntity<PagedResponseDto<UserProfileResponseDto>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userProfileService.listAllUsers(page, size));
    }

    // ── Get by DB id ───────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserProfileResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userProfileService.getById(id));
    }

    // ── Update role ────────────────────────────────────────────────────────────
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user role")
    public ResponseEntity<UserProfileResponseDto> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequestDto dto) {
        return ResponseEntity.ok(userProfileService.updateRole(id, dto));
    }

    // ── Soft-delete ────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft-delete a user")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userProfileService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Search ─────────────────────────────────────────────────────────────────
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Search users by name, email, or role")
    public ResponseEntity<PagedResponseDto<UserProfileResponseDto>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) RoleEnum role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                userProfileService.searchUsers(name, email, role, page, size));
    }

    // ── "Me" endpoint — any authenticated user gets their own profile ──────────
    @GetMapping("/me")
    @Operation(summary = "Get current user's own profile")
    public ResponseEntity<UserProfileResponseDto> getMyProfile(
            @AuthenticationPrincipal GatewayUser principal) {
        return ResponseEntity.ok(userProfileService.getByUserId(principal.getUserId()));
    }
}