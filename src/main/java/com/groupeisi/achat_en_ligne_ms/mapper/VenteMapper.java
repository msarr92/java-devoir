package com.groupeisi.achat_en_ligne_ms.mapper;

import com.groupeisi.achat_en_ligne_ms.dto.VenteDto;
import com.groupeisi.achat_en_ligne_ms.entities.Produits;
import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import com.groupeisi.achat_en_ligne_ms.entities.Ventes;
import org.springframework.stereotype.Component;

@Component
public class VenteMapper {

    public VenteDto toDto(Ventes entity) {
        return VenteDto.builder()
                .id(entity.getId())
                .dateP(entity.getDateP())
                .quantity(entity.getQuantity())
                .productRef(entity.getProduct().getRef())
                .userId(entity.getUser().getId())
                .build();
    }

    public Ventes toEntity(VenteDto dto, Produits produit, UserAccount user) {
        return Ventes.builder()
                .id(dto.getId())
                .dateP(dto.getDateP())
                .quantity(dto.getQuantity())
                .product(produit)
                .user(user)
                .build();
    }
}
