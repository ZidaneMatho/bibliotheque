package com.example.bibliotheque.config;

import com.example.bibliotheque.models.Role;
import com.example.bibliotheque.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // Routes publiques
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/exists/**").permitAll()

                        // Routes accessibles aux membres ET administrateurs (CORRIGÉ)
                        .requestMatchers(HttpMethod.GET, "/api/books/**").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.GET, "/api/books/available").hasAnyRole("ADMIN", "MEMBER")

                        // Routes membres seulement
                        .requestMatchers(HttpMethod.POST, "/api/borrows").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.PUT, "/api/borrows/*/return").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.PUT, "/api/borrows/*/extend").hasRole("MEMBER")

                        // Routes admin seulement
                        .requestMatchers(HttpMethod.POST, "/api/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")
                        .requestMatchers("/api/members/**").hasRole("ADMIN")
                        .requestMatchers("/api/categories/**").hasRole("ADMIN")
                        .requestMatchers("/api/administrators/**").hasRole("ADMIN")

                        // Toute autre requête nécessite authentification
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .httpBasic(httpBasic -> httpBasic.realmName("Bibliotheque API"));

        return http.build();
    }
}