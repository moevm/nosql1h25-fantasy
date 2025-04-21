package org.example.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.dto.CatalogItemDto;
import org.example.backend.dto.EmbeddedPersonDto;
import org.example.backend.dto.ReviewDto;
import org.example.backend.mapper.CatalogItemMapper;
import org.example.backend.model.*;
import org.example.backend.service.CatalogItemService;
import org.example.backend.service.DataImportExportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CatalogControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CatalogItemService catalogItemService;

    @Mock
    private CatalogItemMapper catalogItemMapper;

    @Mock
    private DataImportExportService dataImportExportService;

    @InjectMocks
    private CatalogController catalogController;

    private ObjectMapper objectMapper = new ObjectMapper();
    
    private CatalogItem testCatalogItem;
    private CatalogItemDto testCatalogItemDto;
    private List<CatalogItem> testCatalogItems;
    private List<CatalogItemDto> testCatalogItemDtos;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(catalogController).build();
        
        // Подготовка тестовых данных
        testCatalogItem = new CatalogItem();
        testCatalogItem.setId("1");
        testCatalogItem.setTitle("Test Movie");
        testCatalogItem.setType(ItemType.FILM);
        testCatalogItem.setDescription("Test description");
        testCatalogItem.setStartYear(2021);
        testCatalogItem.setEndYear(2021);
        testCatalogItem.setTags(Arrays.asList(Tag.ADVENTURE));
        testCatalogItem.setRating(8.5);
        testCatalogItem.setCountry(Country.USA);
        testCatalogItem.setDuration(120);

        EmbeddedPerson director = new EmbeddedPerson();
        director.setName("Test Director");
        director.setRole(Role.DIRECTOR);

        Review review = new Review();
        review.setReviewerName("Test Reviewer");
        review.setText("Great movie");
        review.setRating(9.0);

        testCatalogItem.setPersons(Collections.singletonList(director));
        testCatalogItem.setReviews(Collections.singletonList(review));

        testCatalogItemDto = new CatalogItemDto();
        testCatalogItemDto.setId("1");
        testCatalogItemDto.setTitle("Test Movie");
        testCatalogItemDto.setType(ItemType.FILM);
        testCatalogItemDto.setDescription("Test description");
        testCatalogItemDto.setStartYear(2021);
        testCatalogItemDto.setEndYear(2021);
        testCatalogItemDto.setTags(Arrays.asList(Tag.ADVENTURE));
        testCatalogItemDto.setRating(8.5);
        testCatalogItemDto.setCountry(Country.USA);
        testCatalogItemDto.setDuration(120);

        EmbeddedPersonDto directorDto = new EmbeddedPersonDto();
        directorDto.setName("Test Director");
        directorDto.setRole(Role.DIRECTOR);

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewerName("Test Reviewer");
        reviewDto.setText("Great movie");
        reviewDto.setRating(9.0);

        testCatalogItemDto.setPersons(Collections.singletonList(directorDto));
        testCatalogItemDto.setReviews(Collections.singletonList(reviewDto));

        testCatalogItems = Arrays.asList(testCatalogItem);
        testCatalogItemDtos = Arrays.asList(testCatalogItemDto);

        // Настройка поведения моков
        when(catalogItemMapper.toEntity(any(CatalogItemDto.class))).thenReturn(testCatalogItem);
        when(catalogItemMapper.toDto(any(CatalogItem.class))).thenReturn(testCatalogItemDto);
        when(catalogItemMapper.toDtoList(any())).thenReturn(testCatalogItemDtos);
    }

    @Test
    @DisplayName("Should create catalog item successfully")
    void shouldCreateCatalogItem() throws Exception {
        when(catalogItemService.saveItem(any(CatalogItem.class))).thenReturn(testCatalogItem);

        mockMvc.perform(post("/catalog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCatalogItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.title", is("Test Movie")))
                .andExpect(jsonPath("$.type", is("FILM")))
                .andExpect(jsonPath("$.rating", is(8.5)))
                .andExpect(jsonPath("$.country", is("USA")));
    }

    @Test
    @DisplayName("Should find catalog item by ID")
    void shouldFindCatalogItemById() throws Exception {
        when(catalogItemService.getItemById("1")).thenReturn(Optional.of(testCatalogItem));

        mockMvc.perform(get("/catalog/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.title", is("Test Movie")));
    }

    @Test
    @DisplayName("Should return 404 when item not found")
    void shouldReturn404WhenItemNotFound() throws Exception {
        when(catalogItemService.getItemById("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/catalog/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should import catalog successfully")
    void shouldImportCatalogSuccessfully() throws Exception {
        String jsonData = "[{\"title\":\"Test Movie\"}]";
        when(dataImportExportService.importDataFromJson(jsonData)).thenReturn(1);

        mockMvc.perform(post("/catalog/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("Should handle import failure")
    void shouldHandleImportFailure() throws Exception {
        String jsonData = "invalid json";
        when(dataImportExportService.importDataFromJson(jsonData)).thenThrow(new RuntimeException("Invalid JSON"));

        mockMvc.perform(post("/catalog/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should export catalog successfully")
    void shouldExportCatalogSuccessfully() throws Exception {
        when(dataImportExportService.exportCatalog()).thenReturn(testCatalogItemDtos);

        mockMvc.perform(get("/catalog/export"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].title", is("Test Movie")));
    }

    @Test
    @DisplayName("Should get top picks")
    void shouldGetTopPicks() throws Exception {
        when(catalogItemService.getTopRatedItems(anyInt())).thenReturn(testCatalogItems);

        mockMvc.perform(get("/catalog/top-picks")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].rating", is(8.5)));
    }

    @Test
    @DisplayName("Should get top of week")
    void shouldGetTopOfWeek() throws Exception {
        when(catalogItemService.getMostReviewedItems(anyInt())).thenReturn(testCatalogItems);

        mockMvc.perform(get("/catalog/top-week")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")));
    }
}