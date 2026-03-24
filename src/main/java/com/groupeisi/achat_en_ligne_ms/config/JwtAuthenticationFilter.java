package com.groupeisi.achat_en_ligne_ms.config;

import com.groupeisi.achat_en_ligne_ms.security.CustomUserDetailsService;
import com.groupeisi.achat_en_ligne_ms.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Service pour gérer les tokens JWT (génération, validation, extraction)
    private final JwtService jwtService;

    // Service pour charger les informations de l'utilisateur depuis la base
    private final CustomUserDetailsService userDetailsService;

    /*
     * Cette méthode est appelée à chaque requête HTTP.
     * Elle permet de vérifier si un token JWT est présent et valide.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Récupère le header Authorization de la requête
        final String authHeader = request.getHeader("Authorization");

        // Si le header est vide ou ne commence pas par "Bearer ", on passe à la suite sans authentification
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraction du token JWT (on enlève "Bearer ")
        String token = authHeader.substring(7);

        // Extraction du username contenu dans le token
        String username = jwtService.extractUsername(token);

        /*
         * Vérifie :
         * - si le username existe
         * - si aucun utilisateur n'est encore authentifié dans le contexte
         */
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Charger les détails de l'utilisateur depuis la base de données
            var userDetails = userDetailsService.loadUserByUsername(username);

            // Vérifier si le token est valide
            if (jwtService.isTokenValid(token, userDetails)) {

                // Créer un objet d'authentification Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities() // rôles/permissions
                        );

                // Ajouter les détails de la requête (IP, session, etc.)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Mettre l'utilisateur comme authentifié dans le contexte Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continuer la chaîne de filtres (important sinon la requête s'arrête ici)
        filterChain.doFilter(request, response);
    }
}
