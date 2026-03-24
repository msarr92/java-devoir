package com.groupeisi.achat_en_ligne_ms.service.impl;

import com.groupeisi.achat_en_ligne_ms.dto.ProduitDto;
import com.groupeisi.achat_en_ligne_ms.entities.Produits;
import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import com.groupeisi.achat_en_ligne_ms.exception.ResourceNotFoundException;
import com.groupeisi.achat_en_ligne_ms.mapper.ProduitMapper;
import com.groupeisi.achat_en_ligne_ms.repository.ProduitRepository;
import com.groupeisi.achat_en_ligne_ms.repository.UserAccountRepository;
import com.groupeisi.achat_en_ligne_ms.service.KafkaLogService;
import com.groupeisi.achat_en_ligne_ms.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Service métier pour gérer les produits
 * Contient :
 * - accès base de données
 * - cache Redis
 * - logs Kafka
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "produits") // nom du cache Redis
public class ProduitServiceImpl implements ProduitService {

    // Accès aux données
    private final ProduitRepository repository;
    private final UserAccountRepository userRepository;

    // Mapper DTO ↔ Entity
    private final ProduitMapper mapper;

    // Service Kafka pour logs
    private final KafkaLogService kafkaLogService;

    /*
     * Créer un produit
     */
    @Override
    public ProduitDto create(ProduitDto dto) {

        // Vérifier si l'utilisateur existe
        UserAccount user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));

        // Conversion DTO → Entity
        Produits produit = mapper.toEntity(dto, user);

        // Sauvegarde en base
        Produits saved = repository.save(produit);

        // Envoi d’un log dans Kafka
        kafkaLogService.sendLog("Création produit : " + saved.getRef());

        // Retourner DTO
        return mapper.toDto(saved);
    }

    /*
     * Récupérer tous les produits
     */
    @Override
    public List<ProduitDto> getAll() {

        // Conversion de toutes les entités en DTO
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    /*
     * Récupérer un produit par sa référence
     * Utilise Redis (cache)
     */
    @Override
    @Cacheable(key = "#ref") // stocke en cache
    public ProduitDto getById(String ref) {

        // Recherche en base
        Produits produit = repository.findById(ref)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));

        return mapper.toDto(produit);
    }

    /*
     * Modifier un produit
     */
    @Override
    @CachePut(key = "#ref") // met à jour le cache
    public ProduitDto update(String ref, ProduitDto dto) {

        // Vérifier si produit existe
        Produits produit = repository.findById(ref)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));

        // Vérifier utilisateur
        UserAccount user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));

        // Mise à jour des champs
        produit.setName(dto.getName());
        produit.setStock(dto.getStock());
        produit.setUser(user);

        // Sauvegarde
        Produits updated = repository.save(produit);

        // Log Kafka
        kafkaLogService.sendLog("Modification produit : " + updated.getRef());

        return mapper.toDto(updated);
    }

    /*
     * Supprimer un produit
     */
    @Override
    @CacheEvict(key = "#ref") // supprime du cache
    public void delete(String ref) {

        // Suppression en base
        repository.deleteById(ref);

        // Log Kafka
        kafkaLogService.sendLog("Suppression produit : " + ref);
    }
}