package com.info.keycloak.controller;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/user/dashboard")
    public String userAccess() {
        return "User access granted";
    }

    @GetMapping("/admin/dashboard")
    public String adminAccess() {
        return "Admin access granted";
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        System.out.println("Hello from public endpoint!");
        return "Hello from public endpoint!";
    }

    @GetMapping
    @RolesAllowed("ROLE_default-roles-master")
    public String protectedHello(@RequestHeader("Authorization") String authorization) {
        System.out.println("Hello from protected endpoint! authorization: " + authorization);
        return "Hello from protected endpoint!";
    }

    @GetMapping("/secured")
    @PreAuthorize("hasRole('default-roles-master')")
    public String secured() {
        System.out.println("Hello from protected endpoint!");
        return "Hello from protected endpoint!";
    }

    @GetMapping("/debug")
    public ResponseEntity<?> debugRoles(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(jwt.getClaims());
    }


    @GetMapping("/debug/1")
    public String debug(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
       // return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", "));
        Map<String, Object> info = new HashMap<>();

        // Username
        String username = authentication.getName(); // usually the "preferred_username" from JWT
        info.put("username", username);

        // Roles / authorities
        info.put("roles", authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        // JWT claims (if available)
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Map<String, Object> claims = jwtAuth.getToken().getClaims();
            info.put("claims", claims);
        }
        return info.toString();
    }

}
