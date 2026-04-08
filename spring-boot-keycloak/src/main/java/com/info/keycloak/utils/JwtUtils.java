package com.info.keycloak.utils;

import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.*;

public class JwtUtils {

    public static Map<String, Object> decodeJwt(String token) throws ParseException {
        SignedJWT jwt = (SignedJWT) JWTParser.parse(token);
        Map<String, Object> claims = jwt.getJWTClaimsSet().getClaims();

        Map<String, Object> result = new HashMap<>();
        result.put("preferred_username", claims.get("preferred_username"));
        result.put("email", claims.get("email"));

        // Extract roles
        Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            result.put("roles", realmAccess.get("roles"));
        } else {
            result.put("roles", Collections.emptyList());
        }

        return result;
    }
}