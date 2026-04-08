package com.info.keycloak.service;

import com.info.keycloak.config.KeycloakProperties;
import com.info.keycloak.config.KeycloakTokenService;
import com.info.keycloak.dto.KeycloakTokenResponse;
import com.info.keycloak.dto.KeycloakUserRequest;
import com.info.keycloak.dto.LoginDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakAdminService.class);

    private static final String TOKEN_ENDPOINT = "/protocol/openid-connect/token";
    private static final String USERS_ENDPOINT = "/admin/realms/%s/users";

    private final KeycloakTokenService keycloakTokenService;
    private final KeycloakProperties keycloakProperties;

    private final RestTemplate restTemplate;

    public String createUserInKeycloak(String username, String email, String password) {

        KeycloakTokenResponse keycloakTokenResponse = keycloakTokenService.getAccessToken(new LoginDTO(username, null, password));
        String url = buildUsersUrl();

        HttpHeaders headers = buildHeaders(keycloakTokenResponse.getAccessToken());
        KeycloakUserRequest requestBody = buildUserRequest(username, email, password);

        HttpEntity<KeycloakUserRequest> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Void> response =
                    restTemplate.exchange(url, HttpMethod.POST, request, Void.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                String userId = extractUserId(response);
                log.info("User created successfully in Keycloak. userId={}", userId);
                return userId;
            }

            throw new RuntimeException("Unexpected response from Keycloak: " + response.getStatusCode());

        } catch (HttpStatusCodeException ex) {
            log.error("Keycloak user creation failed. status={}, body={}",
                    ex.getStatusCode(), ex.getResponseBodyAsString());

            throw new RuntimeException("Failed to create user in Keycloak", ex);
        }
    }

    private String buildUsersUrl() {
        return String.format("%s" + USERS_ENDPOINT,
                keycloakProperties.getAuthServerUrl(),
                keycloakProperties.getRealm());
    }

    private HttpHeaders buildHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private KeycloakUserRequest buildUserRequest(String username, String email, String password) {
        KeycloakUserRequest user = new KeycloakUserRequest();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

        KeycloakUserRequest.Credential credential = new KeycloakUserRequest.Credential();
        credential.setType("password");
        credential.setValue(password);
        credential.setTemporary(false);

        user.setCredentials(List.of(credential));
        user.setRequiredActions(List.of("CONFIGURE_TOTP"));
        return user;
    }

    private String extractUserId(ResponseEntity<?> response) {
        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);

        if (location == null) {
            throw new RuntimeException("User created but Location header is missing");
        }

        return location.substring(location.lastIndexOf("/") + 1);
    }

}

