package com.supplychain.user.dto;

import com.supplychain.user.enums.RoleEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfileResponseDto {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private RoleEnum role;
    private Boolean isActive;
    private LocalDateTime createdAt;
}