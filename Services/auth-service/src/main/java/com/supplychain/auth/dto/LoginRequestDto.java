package com.supplychain.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginRequestDto {
    @Email @NotBlank
    private String email;

    @NotBlank
    private String password;
}