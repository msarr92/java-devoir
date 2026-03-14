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

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "users")
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository repository;
    private final UserAccountMapper mapper;
    private final KafkaLogService kafkaLogService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserAccountDto create(UserAccountDto dto) {
        UserAccount user = mapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        UserAccount saved = repository.save(user);
        kafkaLogService.sendLog("Création user : " + saved.getEmail());
        return mapper.toDto(saved);
    }

    @Override
    public List<UserAccountDto> getAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    @Cacheable(key = "#id")
    public UserAccountDto getById(Long id) {
        UserAccount user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));
        return mapper.toDto(user);
    }

    @Override
    @CachePut(key = "#id")
    public UserAccountDto update(Long id, UserAccountDto dto) {
        UserAccount user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable"));
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        UserAccount updated = repository.save(user);
        kafkaLogService.sendLog("Modification user : " + updated.getEmail());
        return mapper.toDto(updated);
    }

    @Override
    @CacheEvict(key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
        kafkaLogService.sendLog("Suppression user id : " + id);
    }
}
