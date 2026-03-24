package com.groupeisi.achat_en_ligne_ms.mapper;

import com.groupeisi.achat_en_ligne_ms.dto.VenteDto;
import com.groupeisi.achat_en_ligne_ms.entities.Produits;
import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import com.groupeisi.achat_en_ligne_ms.entities.Ventes;
import org.springframework.stereotype.Component;

/*
 * Mapper pour convertir :
 * - Entity (Ventes) → DTO (VenteDto)
 * - DTO (VenteDto) → Entity (Ventes)
 */
@Component
public class VenteMapper {

    /*
     * Convertit une entité Ventes en DTO
     * Utilisé pour envoyer les données au client
     */
    public VenteDto toDto(Ventes entity) {
        return VenteDto.builder()

                .id(entity.getId()) // ID de la vente
                .dateP(entity.getDateP()) // date de la vente
                .quantity(entity.getQuantity()) // quantité vendue

                // Récupère la référence du produit lié
                .productRef(entity.getProduct().getRef())

                // Récupère l'ID de l'utilisateur lié
                .userId(entity.getUser().getId())

                .build();
    }

    /*
     * Convertit un DTO VenteDto en entité Ventes
     * Utilisé pour enregistrer les données en base
     */
    public Ventes toEntity(VenteDto dto, Produits produit, UserAccount user) {
        return Ventes.builder()

                .id(dto.getId()) // ID (utile pour update)

                .dateP(dto.getDateP()) // date
                .quantity(dto.getQuantity()) // quantité

                // Associe le produit récupéré depuis la base
                .product(produit)

                // Associe l'utilisateur récupéré depuis la base
                .user(user)

                .build();
    }
}