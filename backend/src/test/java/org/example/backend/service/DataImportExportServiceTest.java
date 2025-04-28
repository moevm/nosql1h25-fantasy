package org.example.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.dto.CatalogItemDto;
import org.example.backend.mapper.CatalogItemMapper;
import org.example.backend.model.CatalogItem;
import org.example.backend.model.Country;
import org.example.backend.model.ItemType;
import org.example.backend.repository.CatalogItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataImportExportServiceTest {

    @Mock
    private CatalogItemRepository catalogItemRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CatalogItemMapper catalogItemMapper;

    @InjectMocks
    private DataImportExportService dataImportExportService;

    private CatalogItem testCatalogItem1;
    private CatalogItem testCatalogItem2;
    private CatalogItemDto testCatalogItemDto1;
    private CatalogItemDto testCatalogItemDto2;
    private List<CatalogItem> testCatalogItems;
    private List<CatalogItemDto> testCatalogItemDtos;
    private String validJson;

    @BeforeEach
    void setUp() {
        testCatalogItem1 = new CatalogItem();
        testCatalogItem1.setId("1");
        testCatalogItem1.setTitle("Breaking Bad");
        testCatalogItem1.setType(ItemType.SERIES);
        testCatalogItem1.setRating(9.5);
        testCatalogItem1.setCountry(Country.USA);
        testCatalogItem1.setSeasons(5);

        testCatalogItem2 = new CatalogItem();
        testCatalogItem2.setId("2");
        testCatalogItem2.setTitle("Game of Thrones");
        testCatalogItem2.setType(ItemType.SERIES);
        testCatalogItem2.setRating(9.3);
        testCatalogItem2.setCountry(Country.USA);
        testCatalogItem2.setSeasons(8);

        testCatalogItems = Arrays.asList(testCatalogItem1, testCatalogItem2);

        testCatalogItemDto1 = new CatalogItemDto();
        testCatalogItemDto1.setId("1");
        testCatalogItemDto1.setTitle("Breaking Bad");
        testCatalogItemDto1.setType(ItemType.SERIES);
        testCatalogItemDto1.setRating(9.5);
        testCatalogItemDto1.setCountry(Country.USA);
        testCatalogItemDto1.setSeasons(5);

        testCatalogItemDto2 = new CatalogItemDto();
        testCatalogItemDto2.setId("2");
        testCatalogItemDto2.setTitle("Game of Thrones");
        testCatalogItemDto2.setType(ItemType.SERIES);
        testCatalogItemDto2.setRating(9.3);
        testCatalogItemDto2.setCountry(Country.USA);
        testCatalogItemDto2.setSeasons(8);

        testCatalogItemDtos = Arrays.asList(testCatalogItemDto1, testCatalogItemDto2);

        validJson = "[{\"title\":\"Breaking Bad\",\"type\":\"SERIES\"},{\"title\":\"Game of Thrones\",\"type\":\"SERIES\"}]";
    }

    @Test
    @DisplayName("Should import data from JSON successfully")
    void shouldImportDataFromJsonSuccessfully() throws Exception {
        CatalogItem[] catalogItems = {testCatalogItem1, testCatalogItem2};
        when(objectMapper.readValue(validJson, CatalogItem[].class)).thenReturn(catalogItems);
        when(catalogItemRepository.saveAll(Arrays.asList(catalogItems))).thenReturn(testCatalogItems);

        int result = dataImportExportService.importDataFromJson(validJson);

        assertEquals(2, result);
        verify(catalogItemRepository).saveAll(Arrays.asList(catalogItems));
    }

    @Test
    @DisplayName("Should throw exception when invalid JSON is provided")
    void shouldThrowExceptionWhenInvalidJsonIsProvided() throws Exception {
        String invalidJson = "invalid json format";
        when(objectMapper.readValue(invalidJson, CatalogItem[].class)).thenThrow(new RuntimeException("Invalid JSON"));

        Exception exception = assertThrows(Exception.class, () -> {
            dataImportExportService.importDataFromJson(invalidJson);
        });
        
        assertEquals("Invalid JSON", exception.getMessage());
        
        verify(catalogItemRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("Should export catalog successfully")
    void shouldExportCatalogSuccessfully() {
        when(catalogItemRepository.findAll()).thenReturn(testCatalogItems);
        when(catalogItemMapper.toDtoList(testCatalogItems)).thenReturn(testCatalogItemDtos);

        List<CatalogItemDto> result = dataImportExportService.exportCatalog();

        assertEquals(2, result.size());
        assertEquals("Breaking Bad", result.get(0).getTitle());
        assertEquals("Game of Thrones", result.get(1).getTitle());
        verify(catalogItemRepository).findAll();
        verify(catalogItemMapper).toDtoList(testCatalogItems);
    }

    @Test
    @DisplayName("Should return empty list when no items exist")
    void shouldReturnEmptyListWhenNoItemsExist() {
        when(catalogItemRepository.findAll()).thenReturn(List.of());
        when(catalogItemMapper.toDtoList(List.of())).thenReturn(List.of());

        List<CatalogItemDto> result = dataImportExportService.exportCatalog();

        assertEquals(0, result.size());
        verify(catalogItemRepository).findAll();
        verify(catalogItemMapper).toDtoList(List.of());
    }
} 