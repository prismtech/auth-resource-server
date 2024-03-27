package com.oauth.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
@EnableWebSecurity
public class ResourceServerConfiguration {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String jwkSetUri;
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withJwkSetUri(jwkSetUri)
                .build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/resource-apis/**");
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((req) -> req
                .requestMatchers("/resource-apis/articles-system").access(hasScope("system.read"))
                .requestMatchers("/resource-apis/articles-user").access(hasScope("user.read"))
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer((resourceServer) -> resourceServer.jwt((jwt) -> jwt.decoder(jwtDecoder())));
        return http.build();
    }
}
