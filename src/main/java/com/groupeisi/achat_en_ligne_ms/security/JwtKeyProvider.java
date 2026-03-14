package com.groupeisi.achat_en_ligne_ms.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class JwtKeyProvider {

    private final SecretKey secretKey;
    private final String base64Secret;

    public JwtKeyProvider() {
        byte[] keyBytes = new byte[64]; // 512 bits
        new SecureRandom().nextBytes(keyBytes);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.base64Secret = Base64.getEncoder().encodeToString(keyBytes);

        System.out.println("JWT Secret généré au démarrage : " + base64Secret);
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public String getBase64Secret() {
        return base64Secret;
    }
}
