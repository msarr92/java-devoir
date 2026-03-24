package com.groupeisi.achat_en_ligne_ms.controller;

import com.groupeisi.achat_en_ligne_ms.dto.UserAccountDto;
import com.groupeisi.achat_en_ligne_ms.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Contrôleur REST pour gérer les utilisateurs.
 * Il permet de créer, lire, modifier et supprimer des comptes utilisateurs.
 */
@RestController
@RequestMapping("/api/users") // URL de base pour les utilisateurs
@RequiredArgsConstructor
public class UserAccountController {

    // Injection du service utilisateur (logique métier)
    private final UserAccountService service;

    /*
     * Créer un nouvel utilisateur
     * Endpoint : POST /api/users
     */
    @PostMapping
    public UserAccountDto create(@RequestBody UserAccountDto dto) {
        return service.create(dto);
    }

    /*
     * Récupérer tous les utilisateurs
     * Endpoint : GET /api/users
     */
    @GetMapping
    public List<UserAccountDto> getAll() {
        return service.getAll();
    }

    /*
     * Récupérer un utilisateur par son ID
     * Endpoint : GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public UserAccountDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    /*
     * Modifier un utilisateur existant
     * Endpoint : PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public UserAccountDto update(@PathVariable Long id, @RequestBody UserAccountDto dto) {
        return service.update(id, dto);
    }

    /*
     * Supprimer un utilisateur
     * Endpoint : DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}