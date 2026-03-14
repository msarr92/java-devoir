package com.groupeisi.achat_en_ligne_ms.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "achats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Achats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateP;

    private double quantity;

    @ManyToOne
    @JoinColumn(name = "product_ref")
    private Produits product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;
}