package com.info.keycloak.dto;

import lombok.Data;
import java.util.List;

@Data
public class KeycloakUserRequest {
    private String username;
    private String email;
    private boolean enabled;
    private List<Credential> credentials;
    private List<String> requiredActions;

    @Data
    public static class Credential {
        private String type;
        private String value;
        private boolean temporary;
    }
}