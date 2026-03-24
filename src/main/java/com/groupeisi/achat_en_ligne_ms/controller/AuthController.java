package com.groupeisi.achat_en_ligne_ms.controller;

import com.groupeisi.achat_en_ligne_ms.dto.AuthRequest;
import com.groupeisi.achat_en_ligne_ms.dto.AuthResponse;
import com.groupeisi.achat_en_ligne_ms.dto.UserAccountDto;
import com.groupeisi.achat_en_ligne_ms.security.CustomUserDetailsService;
import com.groupeisi.achat_en_ligne_ms.security.JwtService;
import com.groupeisi.achat_en_ligne_ms.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

/*
 * Contrôleur REST pour gérer l'authentification des utilisateurs.
 * Il permet :
 * - l'inscription (register)
 * - la connexion (login)
 */
@RestController
@RequestMapping("/api/auth") // URL de base pour l'authentification
@RequiredArgsConstructor
public class AuthController {

    // Permet d’authentifier l’utilisateur (email + password)
    private final AuthenticationManager authenticationManager;

    // Service pour générer le token JWT
    private final JwtService jwtService;

    // Service pour charger les informations utilisateur depuis la base
    private final CustomUserDetailsService userDetailsService;

    // Service métier pour gérer les utilisateurs
    private final UserAccountService userAccountService;

    /*
     * Endpoint pour créer un nouvel utilisateur
     * URL : POST /api/auth/register
     */
    @PostMapping("/register")
    public UserAccountDto register(@RequestBody UserAccountDto dto) {

        // Appel du service pour enregistrer l'utilisateur en base
        return userAccountService.create(dto);
    }

    /*
     * Endpoint pour connecter un utilisateur
     * URL : POST /api/auth/login
     */
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        // Vérifie si email + mot de passe sont corrects
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Récupère les informations de l'utilisateur
        var userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // Génère un token JWT pour cet utilisateur
        String token = jwtService.generateToken(userDetails);

        // Retourne le token au client
        return new AuthResponse(token);
    }
}