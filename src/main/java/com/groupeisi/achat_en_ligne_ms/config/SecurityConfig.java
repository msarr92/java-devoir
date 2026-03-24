package com.groupeisi.achat_en_ligne_ms.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * Classe de configuration de la sécurité de l'application.
 * Elle définit :
 * - les routes sécurisées
 * - les routes publiques
 * - le filtre JWT
 * - l'encodeur de mot de passe
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    // Filtre JWT personnalisé pour vérifier le token à chaque requête
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /*
     * Configure la sécurité HTTP (qui a accès à quoi)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactive CSRF (utile pour API REST sans session)
                .csrf(csrf -> csrf.disable())

                // Configuration des autorisations
                .authorizeHttpRequests(auth -> auth

                        // Routes publiques (pas besoin de token)
                        .requestMatchers(
                                "/api/auth/**",        // login & register
                                "/swagger-ui/**",      // interface Swagger
                                "/swagger-ui.html",
                                "/v3/api-docs/**"      // documentation API
                        ).permitAll()

                        // Toutes les autres routes nécessitent une authentification
                        .anyRequest().authenticated()
                )

                // Ajoute le filtre JWT avant le filtre de login standard
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Construit la configuration de sécurité
        return http.build();
    }

    /*
     * Bean pour encoder les mots de passe.
     * BCrypt permet de sécuriser les mots de passe en base.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Bean permettant de gérer l'authentification (login).
     * Utilisé notamment dans AuthController.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}