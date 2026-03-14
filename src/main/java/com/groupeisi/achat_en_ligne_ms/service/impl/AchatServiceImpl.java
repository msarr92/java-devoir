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

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "achats")
public class AchatServiceImpl implements AchatService {

    private final AchatRepository repository;
    private final ProduitRepository produitRepository;
    private final UserAccountRepository userRepository;
    private final AchatMapper mapper;
    private final KafkaLogService kafkaLogService;

    @Override
    public AchatDto create(AchatDto dto) {
        Produits produit = produitRepository.findById(dto.getProductRef())
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));
        UserAccount user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));

        produit.setStock(produit.getStock() + dto.getQuantity());
        produitRepository.save(produit);

        Achats saved = repository.save(mapper.toEntity(dto, produit, user));
        kafkaLogService.sendLog("Création achat id : " + saved.getId());
        return mapper.toDto(saved);
    }

    @Override
    public List<AchatDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    @Cacheable(key = "#id")
    public AchatDto getById(Long id) {
        Achats achat = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achat introuvable"));
        return mapper.toDto(achat);
    }

    @Override
    @CachePut(key = "#id")
    public AchatDto update(Long id, AchatDto dto) {
        Achats achat = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achat introuvable"));
        Produits produit = produitRepository.findById(dto.getProductRef())
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));
        UserAccount user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));

        achat.setDateP(dto.getDateP());
        achat.setQuantity(dto.getQuantity());
        achat.setProduct(produit);
        achat.setUser(user);

        Achats updated = repository.save(achat);
        kafkaLogService.sendLog("Modification achat id : " + updated.getId());
        return mapper.toDto(updated);
    }

    @Override
    @CacheEvict(key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
        kafkaLogService.sendLog("Suppression achat id : " + id);
    }
}