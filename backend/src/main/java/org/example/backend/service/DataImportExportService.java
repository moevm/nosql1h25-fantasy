package org.example.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.CatalogItemDto;
import org.example.backend.mapper.CatalogItemMapper;
import org.example.backend.model.CatalogItem;
import org.example.backend.repository.CatalogItemRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataImportExportService {

    private final CatalogItemRepository catalogItemRepository;
    private final ObjectMapper objectMapper;
    private final CatalogItemMapper catalogItemMapper;

    public int importDataFromJson(String json) throws Exception {
        CatalogItem[] itemsArray = objectMapper.readValue(json, CatalogItem[].class);
        List<CatalogItem> items = Arrays.asList(itemsArray);

        // catalogItemRepository.deleteAll(); - чтобы очищать бд при импорте

        catalogItemRepository.saveAll(items);
        return items.size();
    }

    public List<CatalogItemDto> exportCatalog() {
        List<CatalogItem> items = catalogItemRepository.findAll();
        return catalogItemMapper.toDtoList(items);
    }

}
