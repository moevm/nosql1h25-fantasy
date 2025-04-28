package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.AllFilterRequest;
import org.example.backend.dto.CatalogItemDto;
import org.example.backend.mapper.CatalogItemMapper;
import org.example.backend.model.CatalogItem;
import org.example.backend.service.CatalogItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alls")
public class AllController {

    private final CatalogItemService catalogItemService;
    private final CatalogItemMapper catalogItemMapper;

    @PostMapping
    public List<CatalogItemDto> searchAlls(@RequestBody AllFilterRequest filter,
                                            @RequestParam int page,
                                            @RequestParam int size) {
        List<CatalogItem> alls = catalogItemService.searchAlls(filter, page, size);
        return catalogItemMapper.toDtoList(alls);
    }
}

