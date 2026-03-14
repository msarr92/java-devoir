package com.groupeisi.achat_en_ligne_ms.service;

import com.groupeisi.achat_en_ligne_ms.dto.VenteDto;

import java.util.List;

public interface VenteService {
    VenteDto create(VenteDto dto);
    List<VenteDto> getAll();
    VenteDto getById(Long id);
    VenteDto update(Long id, VenteDto dto);
    void delete(Long id);
}
