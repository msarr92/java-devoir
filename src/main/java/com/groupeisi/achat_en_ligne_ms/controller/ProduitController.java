package com.groupeisi.achat_en_ligne_ms.controller;

import com.groupeisi.achat_en_ligne_ms.dto.ProduitDto;
import com.groupeisi.achat_en_ligne_ms.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Contrôleur REST pour gérer les produits.
 * Il permet de créer, lire, modifier et supprimer des produits.
 */
@RestController
@RequestMapping("/api/produits") // URL de base pour toutes les opérations sur les produits
@RequiredArgsConstructor
public class ProduitController {

    // Injection du service Produit (logique métier)
    private final ProduitService service;

    /*
     * Créer un nouveau produit
     * Endpoint : POST /api/produits
     */
    @PostMapping
    public ProduitDto create(@RequestBody ProduitDto dto) {
        return service.create(dto);
    }

    /*
     * Récupérer tous les produits
     * Endpoint : GET /api/produits
     */
    @GetMapping
    public List<ProduitDto> getAll() {
        return service.getAll();
    }

    /*
     * Récupérer un produit par sa référence (ref)
     * Endpoint : GET /api/produits/{ref}
     */
    @GetMapping("/{ref}")
    public ProduitDto getById(@PathVariable String ref) {
        return service.getById(ref);
    }

    /*
     * Modifier un produit existant
     * Endpoint : PUT /api/produits/{ref}
     */
    @PutMapping("/{ref}")
    public ProduitDto update(@PathVariable String ref, @RequestBody ProduitDto dto) {
        return service.update(ref, dto);
    }

    /*
     * Supprimer un produit
     * Endpoint : DELETE /api/produits/{ref}
     */
    @DeleteMapping("/{ref}")
    public void delete(@PathVariable String ref) {
        service.delete(ref);
    }
}