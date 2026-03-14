package com.groupeisi.achat_en_ligne_ms.controller;

import com.groupeisi.achat_en_ligne_ms.dto.ProduitDto;
import com.groupeisi.achat_en_ligne_ms.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService service;

    @PostMapping
    public ProduitDto create(@RequestBody ProduitDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<ProduitDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{ref}")
    public ProduitDto getById(@PathVariable String ref) {
        return service.getById(ref);
    }

    @PutMapping("/{ref}")
    public ProduitDto update(@PathVariable String ref, @RequestBody ProduitDto dto) {
        return service.update(ref, dto);
    }

    @DeleteMapping("/{ref}")
    public void delete(@PathVariable String ref) {
        service.delete(ref);
    }
}
