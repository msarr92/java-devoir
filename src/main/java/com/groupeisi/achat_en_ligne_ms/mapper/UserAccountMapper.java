package com.groupeisi.achat_en_ligne_ms.mapper;

import com.groupeisi.achat_en_ligne_ms.dto.UserAccountDto;
import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import org.springframework.stereotype.Component;

/*
 * Mapper pour convertir :
 * - Entity (UserAccount) → DTO (UserAccountDto)
 * - DTO (UserAccountDto) → Entity (UserAccount)
 */
@Component
public class UserAccountMapper {

    /*
     * Convertit une entité UserAccount en DTO
     * Utilisé pour envoyer les données au client
     */
    public UserAccountDto toDto(UserAccount entity) {
        return UserAccountDto.builder()

                .id(entity.getId()) // ID de l'utilisateur
                .email(entity.getEmail()) // email

                // ⚠️ IMPORTANT : on ne retourne PAS le mot de passe pour des raisons de sécurité
                // .password(entity.getPassword()) ❌ à ne pas faire

                .build();
    }

    /*
     * Convertit un DTO en entité UserAccount
     * Utilisé pour enregistrer les données en base
     */
    public UserAccount toEntity(UserAccountDto dto) {
        return UserAccount.builder()

                .id(dto.getId()) // ID (utile pour update)
                .email(dto.getEmail()) // email

                // mot de passe brut (sera encodé dans le service)
                .password(dto.getPassword())

                .build();
    }
}