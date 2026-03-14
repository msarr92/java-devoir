package com.groupeisi.achat_en_ligne_ms.mapper;

import com.groupeisi.achat_en_ligne_ms.dto.AchatDto;
import com.groupeisi.achat_en_ligne_ms.entities.Achats;
import com.groupeisi.achat_en_ligne_ms.entities.Produits;
import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class AchatMapper {

    public AchatDto toDto(Achats entity) {
        return AchatDto.builder()
                .id(entity.getId())
                .dateP(entity.getDateP())
                .quantity(entity.getQuantity())
                .productRef(entity.getProduct().getRef())
                .userId(entity.getUser().getId())
                .build();
    }

    public Achats toEntity(AchatDto dto, Produits produit, UserAccount user) {
        return Achats.builder()
                .id(dto.getId())
                .dateP(dto.getDateP())
                .quantity(dto.getQuantity())
                .product(produit)
                .user(user)
                .build();
    }
}
