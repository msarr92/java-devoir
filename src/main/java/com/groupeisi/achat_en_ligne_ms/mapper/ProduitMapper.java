package com.groupeisi.achat_en_ligne_ms.mapper;

import com.groupeisi.achat_en_ligne_ms.dto.ProduitDto;
import com.groupeisi.achat_en_ligne_ms.entities.Produits;
import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class ProduitMapper {

    public ProduitDto toDto(Produits entity) {
        return ProduitDto.builder()
                .ref(entity.getRef())
                .name(entity.getName())
                .stock(entity.getStock())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .build();
    }

    public Produits toEntity(ProduitDto dto, UserAccount user) {
        return Produits.builder()
                .ref(dto.getRef())
                .name(dto.getName())
                .stock(dto.getStock())
                .user(user)
                .build();
    }
}
