package com.groupeisi.achat_en_ligne_ms.repository;

import com.groupeisi.achat_en_ligne_ms.entities.Ventes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenteRepository extends JpaRepository<Ventes, Long> {
}
