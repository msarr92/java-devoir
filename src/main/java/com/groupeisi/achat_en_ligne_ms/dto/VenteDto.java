package com.groupeisi.achat_en_ligne_ms.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenteDto {
    private Long id;
    private LocalDate dateP;
    private double quantity;
    private String productRef;
    private Long userId;
}
