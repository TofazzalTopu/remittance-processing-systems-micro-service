package com.info.keycloak.controller;

import com.info.keycloak.dto.UserDto;
import com.info.keycloak.service.KeycloakAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/user")
public class UserSyncController {

    private final KeycloakAdminService keycloakAdminService;

    public UserSyncController(KeycloakAdminService keycloakAdminService) {
        this.keycloakAdminService = keycloakAdminService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> syncUser(@Valid @RequestBody UserDto userDto) {
        String response = keycloakAdminService.createUserInKeycloak(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

