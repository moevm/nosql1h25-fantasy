package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SeriesFilterRequest;
import org.example.backend.dto.CatalogItemDto;
import org.example.backend.mapper.CatalogItemMapper;
import org.example.backend.model.CatalogItem;
import org.example.backend.service.CatalogItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/series")
public class SeriesController {

    private final CatalogItemService catalogItemService;
    private final CatalogItemMapper catalogItemMapper;

    @GetMapping
    public List<CatalogItemDto> searchSeries(SeriesFilterRequest filter,
                                             @RequestParam(name = "page") int page,
                                             @RequestParam(name = "size") int size) {
        List<CatalogItem> series = catalogItemService.searchSeries(filter, page, size);
        return catalogItemMapper.toDtoList(series);
    }
}
