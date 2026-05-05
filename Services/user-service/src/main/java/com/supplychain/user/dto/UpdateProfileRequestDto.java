package com.supplychain.user.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateProfileRequestDto {
    private String fullName;
    private String phone;
}