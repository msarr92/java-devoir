package com.groupeisi.achat_en_ligne_ms.controller;

import com.groupeisi.achat_en_ligne_ms.dto.VenteDto;
import com.groupeisi.achat_en_ligne_ms.service.VenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Contrôleur REST pour gérer les ventes.
 * Il permet de créer, lire, modifier et supprimer des ventes.
 */
@RestController
@RequestMapping("/api/ventes") // URL de base pour toutes les opérations sur les ventes
@RequiredArgsConstructor
public class VenteController {

    // Injection du service Vente (logique métier)
    private final VenteService service;

    /*
     * Créer une nouvelle vente
     * Endpoint : POST /api/ventes
     */
    @PostMapping
    public VenteDto create(@RequestBody VenteDto dto) {
        return service.create(dto);
    }

    /*
     * Récupérer toutes les ventes
     * Endpoint : GET /api/ventes
     */
    @GetMapping
    public List<VenteDto> getAll() {
        return service.getAll();
    }

    /*
     * Récupérer une vente par son ID
     * Endpoint : GET /api/ventes/{id}
     */
    @GetMapping("/{id}")
    public VenteDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    /*
     * Modifier une vente existante
     * Endpoint : PUT /api/ventes/{id}
     */
    @PutMapping("/{id}")
    public VenteDto update(@PathVariable Long id, @RequestBody VenteDto dto) {
        return service.update(id, dto);
    }

    /*
     * Supprimer une vente
     * Endpoint : DELETE /api/ventes/{id}
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}