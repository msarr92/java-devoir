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

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "ventes")
public class VenteServiceImpl implements VenteService {

    private final VenteRepository repository;
    private final ProduitRepository produitRepository;
    private final UserAccountRepository userRepository;
    private final VenteMapper mapper;
    private final KafkaLogService kafkaLogService;

    @Override
    public VenteDto create(VenteDto dto) {
        Produits produit = produitRepository.findById(dto.getProductRef())
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));
        UserAccount user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));

        if (produit.getStock() < dto.getQuantity()) {
            throw new RuntimeException("Stock insuffisant");
        }

        produit.setStock(produit.getStock() - dto.getQuantity());
        produitRepository.save(produit);

        Ventes saved = repository.save(mapper.toEntity(dto, produit, user));
        kafkaLogService.sendLog("Création vente id : " + saved.getId());
        return mapper.toDto(saved);
    }

    @Override
    public List<VenteDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    @Cacheable(key = "#id")
    public VenteDto getById(Long id) {
        Ventes vente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente introuvable"));
        return mapper.toDto(vente);
    }

    @Override
    @CachePut(key = "#id")
    public VenteDto update(Long id, VenteDto dto) {
        Ventes vente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente introuvable"));
        Produits produit = produitRepository.findById(dto.getProductRef())
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));
        UserAccount user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));

        vente.setDateP(dto.getDateP());
        vente.setQuantity(dto.getQuantity());
        vente.setProduct(produit);
        vente.setUser(user);

        Ventes updated = repository.save(vente);
        kafkaLogService.sendLog("Modification vente id : " + updated.getId());
        return mapper.toDto(updated);
    }

    @Override
    @CacheEvict(key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
        kafkaLogService.sendLog("Suppression vente id : " + id);
    }
}
