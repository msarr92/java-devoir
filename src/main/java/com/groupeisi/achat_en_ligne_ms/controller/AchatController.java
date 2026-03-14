package com.groupeisi.achat_en_ligne_ms.controller;

import com.groupeisi.achat_en_ligne_ms.dto.AchatDto;
import com.groupeisi.achat_en_ligne_ms.service.AchatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achats")
@RequiredArgsConstructor
public class AchatController {

    private final AchatService service;

    @PostMapping
    public AchatDto create(@RequestBody AchatDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<AchatDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AchatDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public AchatDto update(@PathVariable Long id, @RequestBody AchatDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
