package com.jsalopdev.tesisreservatec.config;

import com.jsalopdev.tesisreservatec.util.JwtFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Inyecta el filtro
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/api/usuario/validar", "/admin/login",
                                "/admin/validar", "/admin/logout", "/admin/inicio",
                                "/css/**", "/js/**", "/images/**"
                        ).permitAll()
                        .requestMatchers("/api/**").authenticated() // 👈 proteges /api/**
                        .anyRequest().denyAll() // 👈 opcional, por seguridad
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // 👈 REGISTRO DEL FILTRO
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }

}
