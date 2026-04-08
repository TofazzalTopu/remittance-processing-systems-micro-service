package com.info.keycloak.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WelcomeController {


    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Keycloak secured application!";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Welcome to the Keycloak secured application! You are an admin!";
    }

    @GetMapping("/debug/otp")
    public Map<String, Object> otpStatus(Authentication authentication) {
        Map<String, Object> info = new HashMap<>();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            info.put("username", jwt.getClaimAsString("preferred_username"));
            info.put("otpRequired", jwt.getClaimAsBoolean("otp")); // Keycloak can add custom claim if configured
        }
        return info;
    }
}
