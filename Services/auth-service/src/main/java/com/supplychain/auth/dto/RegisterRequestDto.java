package com.supplychain.auth.dto;

import com.supplychain.auth.enums.RoleEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @Email @NotBlank
    private String email;

    @NotBlank @Size(min = 8)
    private String password;

    @NotBlank
    private String fullName;

    @NotNull
    private RoleEnum role;
}