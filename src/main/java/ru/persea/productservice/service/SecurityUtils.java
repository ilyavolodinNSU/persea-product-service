package ru.persea.productservice.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class SecurityUtils {
    public static UUID getKeycloakUUID() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) 
            return null;

        return UUID.fromString(Optional.ofNullable(jwtAuth
            .getToken()
            .getSubject())
            .orElseThrow(() -> new RuntimeException(
                    "Пользователь не аутентифицирован"
            )));
    }
}
