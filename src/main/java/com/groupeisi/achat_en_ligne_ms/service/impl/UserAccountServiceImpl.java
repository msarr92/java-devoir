package com.groupeisi.achat_en_ligne_ms.service.impl;

import com.groupeisi.achat_en_ligne_ms.dto.UserAccountDto;
import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import com.groupeisi.achat_en_ligne_ms.exception.ResourceNotFoundException;
import com.groupeisi.achat_en_ligne_ms.mapper.UserAccountMapper;
import com.groupeisi.achat_en_ligne_ms.repository.UserAccountRepository;
import com.groupeisi.achat_en_ligne_ms.service.KafkaLogService;
import com.groupeisi.achat_en_ligne_ms.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Service métier pour gérer les utilisateurs
 * Contient :
 * - gestion des mots de passe (sécurité)
 * - cache Redis
 * - logs Kafka
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "users") // cache Redis pour les utilisateurs
public class UserAccountServiceImpl implements UserAccountService {

    // Accès base de données
    private final UserAccountRepository repository;

    // Mapper DTO ↔ Entity
    private final UserAccountMapper mapper;

    // Service Kafka pour logs
    private final KafkaLogService kafkaLogService;

    // Encodeur pour sécuriser les mots de passe (BCrypt)
    private final PasswordEncoder passwordEncoder;

    /*
     * Créer un utilisateur
     */
    @Override
    public UserAccountDto create(UserAccountDto dto) {

        // Conversion DTO → Entity
        UserAccount user = mapper.toEntity(dto);

        // 🔐 Sécurité : encoder le mot de passe avant de sauvegarder
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Sauvegarde en base
        UserAccount saved = repository.save(user);

        // Envoi log Kafka
        kafkaLogService.sendLog("Création user : " + saved.getEmail());

        // Retour DTO (sans password normalement)
        return mapper.toDto(saved);
    }

    /*
     * Récupérer tous les utilisateurs
     */
    @Override
    public List<UserAccountDto> getAll() {

        // Conversion de toutes les entités en DTO
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    /*
     * Récupérer un utilisateur par ID
     * Utilise le cache Redis
     */
    @Override
    @Cacheable(key = "#id") // lecture depuis cache si disponible
    public UserAccountDto getById(Long id) {

        // Recherche en base
        UserAccount user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));

        return mapper.toDto(user);
    }

    /*
     * Modifier un utilisateur
     */
    @Override
    @CachePut(key = "#id") // met à jour le cache
    public UserAccountDto update(Long id, UserAccountDto dto) {

        // Vérifier existence utilisateur
        UserAccount user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));

        // Mise à jour email
        user.setEmail(dto.getEmail());

        // 🔐 Si un nouveau mot de passe est fourni → encoder
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Sauvegarde
        UserAccount updated = repository.save(user);

        // Log Kafka
        kafkaLogService.sendLog("Modification user : " + updated.getEmail());

        return mapper.toDto(updated);
    }

    /*
     * Supprimer un utilisateur
     */
    @Override
    @CacheEvict(key = "#id") // supprimer du cache
    public void delete(Long id) {

        // Suppression en base
        repository.deleteById(id);

        // Log Kafka
        kafkaLogService.sendLog("Suppression user id : " + id);
    }
}