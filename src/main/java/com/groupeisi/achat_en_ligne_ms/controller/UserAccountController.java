package com.groupeisi.achat_en_ligne_ms.controller;

import com.groupeisi.achat_en_ligne_ms.dto.UserAccountDto;
import com.groupeisi.achat_en_ligne_ms.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService service;

    @PostMapping
    public UserAccountDto create(@RequestBody UserAccountDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<UserAccountDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserAccountDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public UserAccountDto update(@PathVariable Long id, @RequestBody UserAccountDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
