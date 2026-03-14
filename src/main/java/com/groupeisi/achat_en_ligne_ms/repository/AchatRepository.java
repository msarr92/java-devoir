package com.groupeisi.achat_en_ligne_ms.repository;

import com.groupeisi.achat_en_ligne_ms.entities.Achats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchatRepository extends JpaRepository<Achats, Long> {
}
