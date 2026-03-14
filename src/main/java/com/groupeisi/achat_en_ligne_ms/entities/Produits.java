package com.groupeisi.achat_en_ligne_ms.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "produits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produits {

    @Id
    @Column(nullable = false, unique = true)
    private String ref;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double stock;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;
}