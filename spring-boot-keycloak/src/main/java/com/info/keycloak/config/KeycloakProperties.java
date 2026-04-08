package com.info.keycloak.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

    @NotBlank
    private String authServerUrl;

    @NotBlank
    private String grantType;

    @NotBlank
    private String realm;

    private Admin admin;

    @Data
    public static class Admin {
        @NotBlank
        private String clientId;
        private String clientSecret;
        private String username;
        private String password;
    }
}