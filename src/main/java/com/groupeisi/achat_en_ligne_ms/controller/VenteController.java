package com.groupeisi.achat_en_ligne_ms.controller;

import com.groupeisi.achat_en_ligne_ms.dto.VenteDto;
import com.groupeisi.achat_en_ligne_ms.service.VenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class VenteController {

    private final VenteService service;

    @PostMapping
    public VenteDto create(@RequestBody VenteDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<VenteDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public VenteDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public VenteDto update(@PathVariable Long id, @RequestBody VenteDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
