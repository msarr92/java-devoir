package com.groupeisi.achat_en_ligne_ms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/*
 * Service pour gérer les tokens JWT :
 * - génération
 * - extraction des informations
 * - validation
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    // Fournisseur de la clé secrète JWT
    private final JwtKeyProvider jwtKeyProvider;

    // Durée de validité du token (ici 24h)
    private static final long EXPIRATION = 86400000; // 24h

    /*
     * Récupère la clé secrète utilisée pour signer et vérifier les tokens
     */
    private SecretKey getSignKey() {
        return jwtKeyProvider.getSecretKey();
    }

    /*
     * Génère un token JWT pour un utilisateur
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()

                // Le "subject" correspond au username (ici email)
                .subject(userDetails.getUsername())

                // Date de création du token
                .issuedAt(new Date())

                // Date d'expiration (maintenant + 24h)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))

                // Signature du token avec la clé secrète
                .signWith(getSignKey())

                // Génère le token final (String)
                .compact();
    }

    /*
     * Extrait le username (email) depuis le token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /*
     * Méthode générique pour extraire une information (claim) du token
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {

        // Analyse et vérifie le token avec la clé secrète
        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Applique la fonction pour récupérer une donnée spécifique
        return resolver.apply(claims);
    }

    /*
     * Vérifie si le token est valide pour un utilisateur donné
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {

        // Récupère le username dans le token
        final String username = extractUsername(token);

        // Vérifie si le username correspond
        return username.equals(userDetails.getUsername());

        // ⚠️ Amélioration possible :
        // vérifier aussi si le token est expiré
    }
}