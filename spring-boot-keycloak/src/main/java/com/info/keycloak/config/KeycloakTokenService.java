package com.info.keycloak.config;

import com.info.keycloak.dto.KeycloakTokenResponse;
import com.info.keycloak.dto.LoginDTO;
import com.info.keycloak.dto.UserDto;
import com.info.keycloak.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakTokenService {

    private final KeycloakProperties keycloakProperties;
    private final RestTemplate restTemplate;

    private static final String TOKEN_ENDPOINT = "/protocol/openid-connect/token";

    /**
     * Fetches access token for a specific user using password grant.
     *
     * @param loginDTO the user data transfer object containing username and password
     * @return access token (JWT)
     */
    public KeycloakTokenResponse getAccessToken(LoginDTO loginDTO) {
        String url = buildTokenUrl();
        HttpEntity<MultiValueMap<String, String>> request = buildPasswordGrantRequest(loginDTO);

        try {
            ResponseEntity<KeycloakTokenResponse> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            request,
                            KeycloakTokenResponse.class
                    );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }

            throw new RuntimeException("Invalid response from Keycloak: " + response.getStatusCode());

        } catch (RestClientException ex) {
            log.error("Keycloak token retrieval failed: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to get Keycloak token", ex);
        }
    }

    /**
     * Builds Keycloak token URL based on realm and auth server URL.
     */
    private String buildTokenUrl() {
        return String.format("%s/realms/%s%s",
                keycloakProperties.getAuthServerUrl(),
                keycloakProperties.getRealm(),
                TOKEN_ENDPOINT);
    }

    /**
     * Build request for password grant type login.
     */
    private HttpEntity<MultiValueMap<String, String>> buildPasswordGrantRequest(LoginDTO loginDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", keycloakProperties.getGrantType());
        body.add("client_id", keycloakProperties.getAdmin().getClientId());

        // Add client secret only if client is confidential
        if (keycloakProperties.getAdmin().getClientSecret() != null && !keycloakProperties.getAdmin().getClientSecret().isEmpty()) {
            body.add("client_secret", keycloakProperties.getAdmin().getClientSecret());
        }

        body.add("username", loginDTO.getUsername());
        body.add("password", loginDTO.getPassword());

        if (loginDTO.getOtp() != null && !loginDTO.getOtp().isEmpty()) {
            body.add("totp", loginDTO.getOtp());
        }
        return new HttpEntity<>(body, headers);
    }
}