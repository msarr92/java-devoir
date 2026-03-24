package com.groupeisi.achat_en_ligne_ms.mapper;

import com.groupeisi.achat_en_ligne_ms.dto.ProduitDto;
import com.groupeisi.achat_en_ligne_ms.entities.Produits;
import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import org.springframework.stereotype.Component;

/*
 * Mapper pour convertir :
 * - Entity (Produits) → DTO (ProduitDto)
 * - DTO (ProduitDto) → Entity (Produits)
 */
@Component
public class ProduitMapper {

    /*
     * Convertit une entité Produits en DTO ProduitDto
     * Utilisé pour envoyer les données au client
     */
    public ProduitDto toDto(Produits entity) {
        return ProduitDto.builder()

                .ref(entity.getRef()) // référence du produit
                .name(entity.getName()) // nom du produit
                .stock(entity.getStock()) // quantité en stock

                // Vérifie si le produit a un utilisateur associé
                // Si oui → retourne son ID
                // Sinon → retourne null
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)

                .build();
    }

    /*
     * Convertit un DTO ProduitDto en entité Produits
     * Utilisé pour enregistrer les données en base
     */
    public Produits toEntity(ProduitDto dto, UserAccount user) {
        return Produits.builder()

                .ref(dto.getRef()) // référence
                .name(dto.getName()) // nom
                .stock(dto.getStock()) // stock

                // Associe l'utilisateur récupéré depuis la base
                .user(user)

                .build();
    }
}