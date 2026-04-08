Here’s an example of how to configure Keycloak with a Spring Boot application. Keycloak can be used for authentication
and authorization in your Spring Boot project by integrating it with Spring Security.

Steps to Configure Keycloak with Spring Boot

##  keycloak
```agsl
docker run -p 8080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.2.4 start-dev
```
This command starts Keycloak exposed on the local port 8080 and creates an initial admin user with the username admin and password admin.

Log in to the Admin Console
Go to the Keycloak Admin Console.

Log in with the username and password you created earlier.

## Follow the guide:
```agsl
https://www.keycloak.org/getting-started/getting-started-docker
```

Add Dependencies

```
Add the required dependencies in your pom.xml for Maven:
<dependencies>
<!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
</dependencies>
```

Create application.properties or application.yml Configure Keycloak settings in the application.properties (or
application.yml):

```
Add below properties:
keycloak.auth-server-url=http://localhost:8080/auth
keycloak.realm=my-realm
keycloak.resource=my-client
keycloak.public-client=true
keycloak.use-resource-role-mappings=true
spring.security.oauth2.client.provider.keycloak.issuer-uri=${keycloak.auth-server-url}/realms/${keycloak.realm}
```

Replace:

1. my-realm with your Keycloak realm name. </br>
2. my-client with the client ID you created in Keycloak.</br>
3. Create a Security Configuration Class</br>
4. Create a Java class to configure Keycloak with Spring Security.</br>


```
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
public class SecurityConfig {

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt());
        return http.build();
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = new KeycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }
}
```

Add Keycloak Adapter Configuration
Create a class to initialize Keycloak:

```
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
}
```

Setup Keycloak

1. Log in to your Keycloak Admin Console.
2. Create a Realm (e.g., my-realm).
3. Add a Client with Access Type set to public.
4. Create a user under the Users section and assign roles if needed.
5. Test the Application

Run the Spring Boot application.</br>
Navigate to a protected endpoint (e.g., /secured) in the browser.</br>
You should be redirected to the Keycloak login page. Log in with the user credentials you created in Keycloak. </br>
This configuration assumes basic integration with Keycloak. You can extend it for features like custom roles,
fine-grained authorization, or using Keycloak’s Admin REST API.</br>