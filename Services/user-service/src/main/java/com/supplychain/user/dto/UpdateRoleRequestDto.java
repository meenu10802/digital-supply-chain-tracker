package com.supplychain.user.dto;

import com.supplychain.user.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateRoleRequestDto {

    @NotNull(message = "role is required")
    private RoleEnum role;
}