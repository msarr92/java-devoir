package com.groupeisi.achat_en_ligne_ms.mapper;

import com.groupeisi.achat_en_ligne_ms.dto.AchatDto;
import com.groupeisi.achat_en_ligne_ms.entities.Achats;
import com.groupeisi.achat_en_ligne_ms.entities.Produits;
import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import org.springframework.stereotype.Component;

/*
 * Mapper pour convertir :
 * - Entity (Achats) → DTO (AchatDto)
 * - DTO (AchatDto) → Entity (Achats)
 */
@Component
public class AchatMapper {

    /*
     * Convertit une entité Achats en DTO AchatDto
     * Utilisé pour envoyer les données au client
     */
    public AchatDto toDto(Achats entity) {
        return AchatDto.builder()
                .id(entity.getId()) // ID de l'achat
                .dateP(entity.getDateP()) // date de l'achat
                .quantity(entity.getQuantity()) // quantité achetée

                // Récupère la référence du produit lié
                .productRef(entity.getProduct().getRef())

                // Récupère l'ID de l'utilisateur lié
                .userId(entity.getUser().getId())

                .build();
    }

    /*
     * Convertit un DTO AchatDto en entité Achats
     * Utilisé pour enregistrer les données en base
     */
    public Achats toEntity(AchatDto dto, Produits produit, UserAccount user) {
        return Achats.builder()
                .id(dto.getId()) // ID (utile pour update)

                .dateP(dto.getDateP()) // date
                .quantity(dto.getQuantity()) // quantité

                // Associe le produit déjà récupéré depuis la base
                .product(produit)

                // Associe l'utilisateur déjà récupéré depuis la base
                .user(user)

                .build();
    }
}