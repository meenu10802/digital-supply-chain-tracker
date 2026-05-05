package com.supplychain.auth.service;

import com.supplychain.auth.dto.*;
import com.supplychain.auth.entity.*;
import com.supplychain.auth.enums.*;
import com.supplychain.auth.exception.AuthException;
import com.supplychain.auth.messaging.UserEventPublisher;
import com.supplychain.auth.repository.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final RefreshTokenRepository rtRepo;
    private final JwtService jwtService;
    private final TokenBlacklistService blacklist;
    private final UserEventPublisher publisher;
    private final PasswordEncoder encoder;

    @Transactional
    public AuthResponseDto register(RegisterRequestDto req) {
        if (userRepo.existsByEmail(req.getEmail()))
            throw new AuthException("Email already in use");

        User user = User.builder()
                .email(req.getEmail())
                .passwordHash(encoder.encode(req.getPassword()))
                .fullName(req.getFullName())
                .roles(new HashSet<>(Set.of(req.getRole())))
                .provider(AuthProvider.LOCAL)
                .build();

        user = userRepo.save(user);
        publisher.publishUserRegistered(user);
        return buildResponse(user);
    }
    public AuthResponseDto login(LoginRequestDto req) {
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() ->
                        new AuthException("Invalid credentials"));

        if (!encoder.matches(req.getPassword(),
                user.getPasswordHash()))
            throw new AuthException("Invalid credentials");

        return buildResponse(user);
    }

    public AuthResponseDto refresh(RefreshTokenRequestDto req) {
        RefreshToken rt = rtRepo.findByToken(req.getRefreshToken())
                .orElseThrow(() ->
                        new AuthException("Refresh token not found"));

        if (rt.getIsRevoked() ||
                rt.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new AuthException("Refresh token expired");

        User user = userRepo.findById(rt.getUserId())
                .orElseThrow(() ->
                        new AuthException("User not found"));

        return buildResponse(user);
    }

    @Transactional
    public void logout(String token) {
        String jti = jwtService.extractJti(token);
        long remaining = jwtService.getAccessTokenExpiry();
        blacklist.blacklist(jti, remaining);
        rtRepo.deleteByUserId(
                Long.parseLong(
                        jwtService.extractAllClaims(token).getSubject()));
    }

    public AuthResponseDto getMe(String token) {
        Claims claims = jwtService.extractAllClaims(token);
        User user = userRepo
                .findById(Long.parseLong(claims.getSubject()))
                .orElseThrow(() ->
                        new AuthException("User not found"));
        return buildResponse(user);
    }

    @Transactional
    public void assignRole(RoleAssignmentDto dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() ->
                        new AuthException("User not found"));
        user.getRoles().add(dto.getRole());
        userRepo.save(user);
    }

    private AuthResponseDto buildResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = UUID.randomUUID().toString();

        rtRepo.save(RefreshToken.builder()
                .userId(user.getId())
                .token(refreshToken)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .isRevoked(false)
                .build());

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpiry())
                .userId(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()))
                .build();
    }
}