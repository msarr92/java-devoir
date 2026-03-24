package com.groupeisi.achat_en_ligne_ms.security;

import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import com.groupeisi.achat_en_ligne_ms.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/*
 * Service utilisé par Spring Security pour charger les utilisateurs
 * lors de l'authentification (login).
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // Accès à la base de données pour récupérer les utilisateurs
    private final UserAccountRepository repository;

    /*
     * Méthode appelée automatiquement par Spring Security
     * pour charger un utilisateur à partir de son email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Recherche de l'utilisateur en base avec son email
        UserAccount user = repository.findByEmail(email)

                // Si l'utilisateur n'existe pas → exception
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        /*
         * Création d'un objet UserDetails utilisé par Spring Security
         * Cet objet contient :
         * - username (email)
         * - password (crypté)
         * - rôles (authorities)
         */
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()) // email utilisé comme identifiant
                .password(user.getPassword()) // mot de passe (crypté en base)
                .authorities("USER") // rôle de l'utilisateur
                .build();
    }
}