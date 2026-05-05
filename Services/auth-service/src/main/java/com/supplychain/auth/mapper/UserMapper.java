package com.supplychain.auth.mapper;

import com.supplychain.auth.dto.AuthResponseDto;
import com.supplychain.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "tokenType", ignore = true)
    @Mapping(target = "expiresIn", ignore = true)
    @Mapping(target = "roles", ignore = true)
    AuthResponseDto toDto(User user);
}