package com.groupeisi.achat_en_ligne_ms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduitDto {
    private String ref;
    private String name;
    private double stock;
    private Long userId;
}
