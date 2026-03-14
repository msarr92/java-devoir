package com.groupeisi.achat_en_ligne_ms.mapper;

import com.groupeisi.achat_en_ligne_ms.dto.UserAccountDto;
import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class UserAccountMapper {

    public UserAccountDto toDto(UserAccount entity) {
        return UserAccountDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .build();
    }

    public UserAccount toEntity(UserAccountDto dto) {
        return UserAccount.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }
}
