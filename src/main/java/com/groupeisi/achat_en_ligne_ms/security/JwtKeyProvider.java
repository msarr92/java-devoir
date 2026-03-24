package com.groupeisi.achat_en_ligne_ms.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;

/*
 * Cette classe permet de générer une clé secrète JWT automatiquement
 * au démarrage de l'application.
 */
@Component
public class JwtKeyProvider {

    // Clé secrète utilisée pour signer les tokens JWT
    private final SecretKey secretKey;

    // Version de la clé encodée en Base64 (utile pour affichage ou debug)
    private final String base64Secret;

    /*
     * Constructeur appelé au démarrage de l'application
     */
    public JwtKeyProvider() {

        // Création d'un tableau de 64 bytes (512 bits)
        byte[] keyBytes = new byte[64];

        // Génère des valeurs aléatoires sécurisées
        new SecureRandom().nextBytes(keyBytes);

        // Création de la clé secrète utilisée pour signer le JWT
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);

        // Conversion de la clé en format Base64 (lisible)
        this.base64Secret = Base64.getEncoder().encodeToString(keyBytes);

        // Affiche la clé dans la console (utile pour debug)
        System.out.println("JWT Secret généré au démarrage : " + base64Secret);
    }

    /*
     * Retourne la clé secrète utilisée pour signer les tokens
     */
    public SecretKey getSecretKey() {
        return secretKey;
    }

    /*
     * Retourne la clé en format Base64
     */
    public String getBase64Secret() {
        return base64Secret;
    }
}