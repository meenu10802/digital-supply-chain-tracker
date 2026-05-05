package com.supplychain.auth.dto;

import lombok.*;
import java.util.Set;

@Data @Builder
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private Long userId;
    private String email;
    private Set<String> roles;
}