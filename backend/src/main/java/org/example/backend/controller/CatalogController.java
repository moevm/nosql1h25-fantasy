package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.CatalogItemDto;
import org.example.backend.mapper.CatalogItemMapper;
import org.example.backend.model.CatalogItem;
import org.example.backend.service.CatalogItemService;
import org.example.backend.service.DataImportExportService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog")
public class CatalogController {

    private final CatalogItemService catalogItemService;
    private final CatalogItemMapper catalogItemMapper;
    private final DataImportExportService dataImportExportService;

    @PostMapping
    public CatalogItemDto createCatalogItem(@RequestBody CatalogItemDto catalogItemDto) {
        CatalogItem item = catalogItemMapper.toEntity(catalogItemDto);
        CatalogItem saved = catalogItemService.saveItem(item);
        return catalogItemMapper.toDto(saved);
    }

    @GetMapping("/{id}")
    public CatalogItemDto findCatalogItemById(@PathVariable("id") String id) {
        CatalogItem item = catalogItemService.getItemById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catalog item not found"));
        return catalogItemMapper.toDto(item);
    }

    @PostMapping("/import")
    public int importCatalog(@RequestBody String jsonData) {
        try {
            return dataImportExportService.importDataFromJson(jsonData);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to import catalog data", e);
        }
    }

    @GetMapping("/export")
    public List<CatalogItemDto> exportCatalog() {
        return dataImportExportService.exportCatalog();
    }

    @GetMapping("/top-picks")
    public List<CatalogItemDto> getTopPicks(@RequestParam(name = "limit", defaultValue = "10") int limit) {
        List<CatalogItem> topItems = catalogItemService.getTopRatedItems(limit);
        return catalogItemMapper.toDtoList(topItems);
    }

    @GetMapping("/top-week")
    public List<CatalogItemDto> getTopOfWeek(@RequestParam(name = "limit", defaultValue = "10") int limit) {
        List<CatalogItem> mostReviewed = catalogItemService.getMostReviewedItems(limit);
        return catalogItemMapper.toDtoList(mostReviewed);
    }
}
