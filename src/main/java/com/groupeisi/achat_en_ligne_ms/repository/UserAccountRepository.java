package com.groupeisi.achat_en_ligne_ms.repository;

import com.groupeisi.achat_en_ligne_ms.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);
}
