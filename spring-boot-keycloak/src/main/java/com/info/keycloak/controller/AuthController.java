package com.info.keycloak.controller;

import com.info.keycloak.config.KeycloakTokenService;
import com.info.keycloak.dto.KeycloakTokenResponse;
import com.info.keycloak.dto.LoginDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakTokenService keycloakTokenService;

    @PostMapping("/login")
    public ResponseEntity<KeycloakTokenResponse> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(keycloakTokenService.getAccessToken(loginDTO));
    }
}
