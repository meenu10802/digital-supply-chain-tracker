package com.supplychain.auth.security;

import com.supplychain.auth.service.JwtService;
import com.supplychain.auth.service.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenBlacklistService blacklist;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String token = header.substring(7);

        // In JwtAuthFilter, replace your isTokenValid block with this:
        try {
            if (!jwtService.isTokenValid(token)) {
                System.out.println(">>> TOKEN INVALID - filter passing without auth");
                chain.doFilter(req, res);
                return;
            }
        } catch (Exception e) {
            System.out.println(">>> TOKEN EXCEPTION: " + e.getMessage());
            chain.doFilter(req, res);
            return;
        }

        Claims claims = jwtService.extractAllClaims(token);
        String jti = claims.getId();

        try {
            if (blacklist.isBlacklisted(jti)) {
                SecurityContextHolder.clearContext();
                chain.doFilter(req, res);
                return;
            }
        } catch (Exception e) {
            System.out.println("Redis unavailable, skipping blacklist check: " + e.getMessage());
        }

        List<String> roles = claims.get("roles", List.class);

        // Roles in JWT are already "ADMIN", "SUPPLIER" etc.
        // SimpleGrantedAuthority needs exactly "ROLE_ADMIN" for hasRole() to work
        // hasRole("ADMIN") checks for authority "ROLE_ADMIN"
        var authorities = roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toList());

        var auth = new UsernamePasswordAuthenticationToken(
                claims.getSubject(), null, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);
        System.out.println("Auth set: " + auth.getName() + " | Authorities: " + auth.getAuthorities());

        chain.doFilter(req, res);
    }
}