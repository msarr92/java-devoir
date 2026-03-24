package com.groupeisi.achat_en_ligne_ms.controller;

import com.groupeisi.achat_en_ligne_ms.dto.AchatDto;
import com.groupeisi.achat_en_ligne_ms.service.AchatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Contrôleur REST pour gérer les opérations liées aux achats.
 * Il expose les endpoints API pour créer, lire, modifier et supprimer des achats.
 */
@RestController
@RequestMapping("/api/achats") // URL de base pour toutes les routes de ce controller
@RequiredArgsConstructor
public class AchatController {

    // Injection du service Achat (logique métier)
    private final AchatService service;

    /*
     * Créer un nouvel achat
     * Endpoint : POST /api/achats
     */
    @PostMapping
    public AchatDto create(@RequestBody AchatDto dto) {
        return service.create(dto);
    }

    /*
     * Récupérer tous les achats
     * Endpoint : GET /api/achats
     */
    @GetMapping
    public List<AchatDto> getAll() {
        return service.getAll();
    }

    /*
     * Récupérer un achat par son ID
     * Endpoint : GET /api/achats/{id}
     */
    @GetMapping("/{id}")
    public AchatDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    /*
     * Modifier un achat existant
     * Endpoint : PUT /api/achats/{id}
     */
    @PutMapping("/{id}")
    public AchatDto update(@PathVariable Long id, @RequestBody AchatDto dto) {
        return service.update(id, dto);
    }

    /*
     * Supprimer un achat
     * Endpoint : DELETE /api/achats/{id}
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}