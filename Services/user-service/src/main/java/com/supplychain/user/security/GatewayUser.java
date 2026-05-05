package com.supplychain.user.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GatewayUser {
    private final Long userId;
    private final String email;
    private final String roles;
}