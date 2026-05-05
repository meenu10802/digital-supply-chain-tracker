package com.supplychain.user.service;

import com.supplychain.user.dto.*;
import com.supplychain.user.entity.UserProfile;
import com.supplychain.user.enums.RoleEnum;
import com.supplychain.user.exception.DuplicateResourceException;
import com.supplychain.user.exception.ResourceNotFoundException;
import com.supplychain.user.mapper.UserProfileMapper;
import com.supplychain.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    // ── Internal / Service-to-service: create a profile ──────────────────────
    @Transactional
    public UserProfileResponseDto createProfile(CreateUserProfileRequestDto dto) {
        if (userProfileRepository.existsByUserId(dto.getUserId())) {
            throw new DuplicateResourceException(
                    "Profile already exists for userId: " + dto.getUserId());
        }
        if (userProfileRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already in use: " + dto.getEmail());
        }
        UserProfile profile = userProfileMapper.toEntity(dto);
        profile.setIsActive(true);
        UserProfile saved = userProfileRepository.save(profile);
        log.info("UserProfile created for userId={}", saved.getUserId());
        return userProfileMapper.toDto(saved);
    }

    // ── List all active users (paginated) ─────────────────────────────────────
    @Transactional(readOnly = true)
    public PagedResponseDto<UserProfileResponseDto> listAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserProfile> result = userProfileRepository.findByIsActiveTrue(pageable);
        return buildPagedResponse(result);
    }

    // ── Get by internal DB id ─────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public UserProfileResponseDto getById(Long id) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userProfileMapper.toDto(profile);
    }

    // ── Get by userId (used by Feign consumers) ───────────────────────────────
    @Transactional(readOnly = true)
    public UserProfileResponseDto getByUserId(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with userId: " + userId));
        return userProfileMapper.toDto(profile);
    }

    // ── Update role (ADMIN only) ───────────────────────────────────────────────
    @Transactional
    public UserProfileResponseDto updateRole(Long id, UpdateRoleRequestDto dto) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        profile.setRole(dto.getRole());
        log.info("Role updated to {} for userId={}", dto.getRole(), profile.getUserId());
        return userProfileMapper.toDto(userProfileRepository.save(profile));
    }

    // ── Soft-delete (ADMIN only) ───────────────────────────────────────────────
    @Transactional
    public void softDelete(Long id) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        profile.setIsActive(false);
        userProfileRepository.save(profile);
        log.info("UserProfile soft-deleted for id={}", id);
    }

    // ── Search ─────────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public PagedResponseDto<UserProfileResponseDto> searchUsers(
            String name, String email, RoleEnum role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserProfile> result =
                userProfileRepository.searchUsers(name, email, role, pageable);
        return buildPagedResponse(result);
    }

    // ── Helper ─────────────────────────────────────────────────────────────────
    private PagedResponseDto<UserProfileResponseDto> buildPagedResponse(Page<UserProfile> page) {
        return PagedResponseDto.<UserProfileResponseDto>builder()
                .content(page.getContent().stream().map(userProfileMapper::toDto).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}