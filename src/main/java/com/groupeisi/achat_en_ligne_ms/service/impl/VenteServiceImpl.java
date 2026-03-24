package com.groupeisi.achat_en_ligne_ms.service.impl;

import com.groupeisi.achat_en_ligne_ms.dto.VenteDto;
import com.groupeisi.achat_en_ligne_ms.entities.Produits;
import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import com.groupeisi.achat_en_ligne_ms.entities.Ventes;
import com.groupeisi.achat_en_ligne_ms.exception.ResourceNotFoundException;
import com.groupeisi.achat_en_ligne_ms.mapper.VenteMapper;
import com.groupeisi.achat_en_ligne_ms.repository.ProduitRepository;
import com.groupeisi.achat_en_ligne_ms.repository.UserAccountRepository;
import com.groupeisi.achat_en_ligne_ms.repository.VenteRepository;
import com.groupeisi.achat_en_ligne_ms.service.KafkaLogService;
import com.groupeisi.achat_en_ligne_ms.service.VenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Service métier pour gérer les ventes
 * Contient :
 * - logique de diminution du stock
 * - cache Redis
 * - logs Kafka
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "ventes") // cache Redis pour ventes
public class VenteServiceImpl implements VenteService {

    // Accès base de données
    private final VenteRepository repository;
    private final ProduitRepository produitRepository;
    private final UserAccountRepository userRepository;

    // Mapper DTO ↔ Entity
    private final VenteMapper mapper;

    // Service Kafka pour logs
    private final KafkaLogService kafkaLogService;

    /*
     * Créer une vente
     */
    @Override
    public VenteDto create(VenteDto dto) {

        // Récupérer le produit
        Produits produit = produitRepository.findById(dto.getProductRef())
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));

        // Récupérer l'utilisateur
        UserAccount user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));

        // 🔥 LOGIQUE MÉTIER : vérifier stock
        if (produit.getStock() < dto.getQuantity()) {
            throw new RuntimeException("Stock insuffisant");
        }

        // 🔥 LOGIQUE MÉTIER : diminuer le stock
        produit.setStock(produit.getStock() - dto.getQuantity());
        produitRepository.save(produit);

        // Enregistrer la vente
        Ventes saved = repository.save(mapper.toEntity(dto, produit, user));

        // Log Kafka
        kafkaLogService.sendLog("Création vente id : " + saved.getId());

        return mapper.toDto(saved);
    }

    /*
     * Récupérer toutes les ventes
     */
    @Override
    public List<VenteDto> getAll() {

        // Conversion en DTO
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    /*
     * Récupérer une vente par ID
     * Utilise Redis
     */
    @Override
    @Cacheable(key = "#id")
    public VenteDto getById(Long id) {

        Ventes vente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente introuvable"));

        return mapper.toDto(vente);
    }

    /*
     * Modifier une vente
     */
    @Override
    @CachePut(key = "#id")
    public VenteDto update(Long id, VenteDto dto) {

        // Vérifier vente
        Ventes vente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente introuvable"));

        // Vérifier produit
        Produits produit = produitRepository.findById(dto.getProductRef())
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));

        // Vérifier utilisateur
        UserAccount user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));

        // Mise à jour des champs
        vente.setDateP(dto.getDateP());
        vente.setQuantity(dto.getQuantity());
        vente.setProduct(produit);
        vente.setUser(user);

        // ⚠️ ATTENTION :
        // ici tu ne mets pas à jour le stock (problème logique possible)

        // Sauvegarde
        Ventes updated = repository.save(vente);

        // Log Kafka
        kafkaLogService.sendLog("Modification vente id : " + updated.getId());

        return mapper.toDto(updated);
    }

    /*
     * Supprimer une vente
     */
    @Override
    @CacheEvict(key = "#id")
    public void delete(Long id) {

        // Suppression en base
        repository.deleteById(id);

        // Log Kafka
        kafkaLogService.sendLog("Suppression vente id : " + id);
    }
}