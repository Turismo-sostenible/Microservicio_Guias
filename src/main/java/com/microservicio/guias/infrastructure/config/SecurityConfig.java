package com.microservicio.guias.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF (Cross-Site Request Forgery), común para APIs stateless
            .csrf(csrf -> csrf.disable())
            // Definir las reglas de autorización
            .authorizeHttpRequests(auth -> auth
                // Permitir todas las peticiones a cualquier endpoint sin autenticación
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
}