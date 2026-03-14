package com.groupeisi.achat_en_ligne_ms.repository;

import com.groupeisi.achat_en_ligne_ms.entities.Produits;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produits, String> {
}
