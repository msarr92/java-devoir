package com.groupeisi.achat_en_ligne_ms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountDto {
    private Long id;
    private String email;
    private String password;
}
