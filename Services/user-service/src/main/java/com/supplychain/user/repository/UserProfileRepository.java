package com.supplychain.user.repository;

import com.supplychain.user.entity.UserProfile;
import com.supplychain.user.enums.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserId(Long userId);

    Optional<UserProfile> findByEmail(String email);

    boolean existsByUserId(Long userId);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserProfile u WHERE u.isActive = true AND " +
            "(:name IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:role IS NULL OR u.role = :role)")
    Page<UserProfile> searchUsers(
            @Param("name") String name,
            @Param("email") String email,
            @Param("role") RoleEnum role,
            Pageable pageable);

    Page<UserProfile> findByIsActiveTrue(Pageable pageable);
}