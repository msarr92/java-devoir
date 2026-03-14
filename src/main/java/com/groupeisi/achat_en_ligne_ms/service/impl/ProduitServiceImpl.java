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

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "produits")
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository repository;
    private final UserAccountRepository userRepository;
    private final ProduitMapper mapper;
    private final KafkaLogService kafkaLogService;

    @Override
    public ProduitDto create(ProduitDto dto) {
        UserAccount user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));
        Produits produit = mapper.toEntity(dto, user);
        Produits saved = repository.save(produit);
        kafkaLogService.sendLog("Création produit : " + saved.getRef());
        return mapper.toDto(saved);
    }

    @Override
    public List<ProduitDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    @Cacheable(key = "#ref")
    public ProduitDto getById(String ref) {
        Produits produit = repository.findById(ref)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));
        return mapper.toDto(produit);
    }

    @Override
    @CachePut(key = "#ref")
    public ProduitDto update(String ref, ProduitDto dto) {
        Produits produit = repository.findById(ref)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));
        UserAccount user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));

        produit.setName(dto.getName());
        produit.setStock(dto.getStock());
        produit.setUser(user);

        Produits updated = repository.save(produit);
        kafkaLogService.sendLog("Modification produit : " + updated.getRef());
        return mapper.toDto(updated);
    }

    @Override
    @CacheEvict(key = "#ref")
    public void delete(String ref) {
        repository.deleteById(ref);
        kafkaLogService.sendLog("Suppression produit : " + ref);
    }
}
