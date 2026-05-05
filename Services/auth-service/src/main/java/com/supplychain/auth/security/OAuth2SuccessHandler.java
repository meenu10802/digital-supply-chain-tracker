package com.supplychain.auth.security;

import com.supplychain.auth.entity.User;
import com.supplychain.auth.enums.*;
import com.supplychain.auth.messaging.UserEventPublisher;
import com.supplychain.auth.repository.UserRepository;
import com.supplychain.auth.service.JwtService;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication
        .SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final UserEventPublisher publisher;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse res,
                                        Authentication auth) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) auth.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getName();

        AuthProvider provider = req.getRequestURI()
                .contains("google")
                ? AuthProvider.GOOGLE : AuthProvider.GITHUB;

        User user = userRepo
                .findByProviderIdAndProvider(providerId, provider)
                .orElseGet(() -> {
                    User u = User.builder()
                            .email(email)
                            .fullName(name)
                            .providerId(providerId)
                            .provider(provider)
                            .roles(Set.of(RoleEnum.SUPPLIER))
                            .build();
                    u = userRepo.save(u);
                    publisher.publishUserRegistered(u);
                    return u;
                });

        String token = jwtService.generateAccessToken(user);
        res.sendRedirect(
                "/oauth2/callback?token=" + token);
    }
}