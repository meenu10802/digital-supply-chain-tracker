package com.supplychain.auth.dto;

import com.supplychain.auth.enums.RoleEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RoleAssignmentDto {
    @NotNull
    private Long userId;

    @NotNull
    private RoleEnum role;
}