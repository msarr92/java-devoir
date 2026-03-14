package com.groupeisi.achat_en_ligne_ms.service;

import com.groupeisi.achat_en_ligne_ms.dto.UserAccountDto;

import java.util.List;

public interface UserAccountService {
    UserAccountDto create(UserAccountDto dto);
    List<UserAccountDto> getAll();
    UserAccountDto getById(Long id);
    UserAccountDto update(Long id, UserAccountDto dto);
    void delete(Long id);
}
