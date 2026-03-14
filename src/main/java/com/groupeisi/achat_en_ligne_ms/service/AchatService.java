package com.groupeisi.achat_en_ligne_ms.service;

import com.groupeisi.achat_en_ligne_ms.dto.AchatDto;

import java.util.List;

public interface AchatService {
    AchatDto create(AchatDto dto);
    List<AchatDto> getAll();
    AchatDto getById(Long id);
    AchatDto update(Long id, AchatDto dto);
    void delete(Long id);
}
