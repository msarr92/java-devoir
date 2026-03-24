package com.groupeisi.achat_en_ligne_ms.service.impl;

import com.groupeisi.achat_en_ligne_ms.dto.AchatDto;
import com.groupeisi.achat_en_ligne_ms.entities.Achats;
import com.groupeisi.achat_en_ligne_ms.entities.Produits;
import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import com.groupeisi.achat_en_ligne_ms.exception.ResourceNotFoundException;
import com.groupeisi.achat_en_ligne_ms.mapper.AchatMapper;
import com.groupeisi.achat_en_ligne_ms.repository.AchatRepository;
import com.groupeisi.achat_en_ligne_ms.repository.ProduitRepository;
import com.groupeisi.achat_en_ligne_ms.repository.UserAccountRepository;
import com.groupeisi.achat_en_ligne_ms.service.AchatService;
import com.groupeisi.achat_en_ligne_ms.service.KafkaLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Service métier pour gérer les achats
 * C’est ici que se trouve toute la logique :
 * - gestion du stock
 * - appels base de données
 * - cache Redis
 * - logs Kafka
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "achats") // nom du cache Redis
public class AchatServiceImpl implements AchatService {

    // Accès aux données
    private final AchatRepository repository;
    private final ProduitRepository produitRepository;
    private final UserAccountRepository userRepository;

    // Mapper pour convertir DTO ↔ Entity
    private final AchatMapper mapper;

    // Service Kafka pour envoyer des logs
    private final KafkaLogService kafkaLogService;

    /*
     * Créer un achat
     */
    @Override
    public AchatDto create(AchatDto dto) {

        // Récupérer le produit
        Produits produit = produitRepository.findById(dto.getProductRef())
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));

        // Récupérer l'utilisateur
        UserAccount user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));

        // LOGIQUE MÉTIER : augmenter le stock (achat = entrée de stock)
        produit.setStock(produit.getStock() + dto.getQuantity());
        produitRepository.save(produit);

        // Enregistrer l'achat
        Achats saved = repository.save(mapper.toEntity(dto, produit, user));

        // Envoyer un log dans Kafka
        kafkaLogService.sendLog("Création achat id : " + saved.getId());

        // Retourner le DTO
        return mapper.toDto(saved);
    }

    /*
     * Récupérer tous les achats
     */
    @Override
    public List<AchatDto> getAll() {

        // Conversion de toutes les entités en DTO
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    /*
     * Récupérer un achat par ID
     * Utilise le cache Redis
     */
    @Override
    @Cacheable(key = "#id") // stocke le résultat en cache
    public AchatDto getById(Long id) {

        // Recherche en base
        Achats achat = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achat introuvable"));

        return mapper.toDto(achat);
    }

    /*
     * Modifier un achat
     */
    @Override
    @CachePut(key = "#id") // met à jour le cache
    public AchatDto update(Long id, AchatDto dto) {

        // Vérifier existence achat
        Achats achat = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achat introuvable"));

        // Vérifier produit
        Produits produit = produitRepository.findById(dto.getProductRef())
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));

        // Vérifier utilisateur
        UserAccount user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));

        // Mise à jour des champs
        achat.setDateP(dto.getDateP());
        achat.setQuantity(dto.getQuantity());
        achat.setProduct(produit);
        achat.setUser(user);

        // Sauvegarde
        Achats updated = repository.save(achat);

        // Log Kafka
        kafkaLogService.sendLog("Modification achat id : " + updated.getId());

        return mapper.toDto(updated);
    }

    /*
     * Supprimer un achat
     */
    @Override
    @CacheEvict(key = "#id") // supprime du cache
    public void delete(Long id) {

        // Suppression en base
        repository.deleteById(id);

        // Log Kafka
        kafkaLogService.sendLog("Suppression achat id : " + id);
    }
}