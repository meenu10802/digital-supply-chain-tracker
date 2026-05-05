package com.supplychain.user.dto;

import com.supplychain.user.enums.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateUserProfileRequestDto {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotBlank(message = "fullName is required")
    private String fullName;

    @Email @NotBlank(message = "email is required")
    private String email;

    private String phone;

    @NotNull(message = "role is required")
    private RoleEnum role;
}