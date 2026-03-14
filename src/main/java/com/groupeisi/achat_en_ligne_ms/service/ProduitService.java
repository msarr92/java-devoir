package com.groupeisi.achat_en_ligne_ms.service;

import com.groupeisi.achat_en_ligne_ms.dto.ProduitDto;

import java.util.List;

public interface ProduitService {
    ProduitDto create(ProduitDto dto);
    List<ProduitDto> getAll();
    ProduitDto getById(String ref);
    ProduitDto update(String ref, ProduitDto dto);
    void delete(String ref);
}
