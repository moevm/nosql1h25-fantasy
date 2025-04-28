package org.example.backend.controller;

import org.example.backend.dto.CatalogItemDto;
import org.example.backend.dto.SeriesFilterRequest;
import org.example.backend.mapper.CatalogItemMapper;
import org.example.backend.model.*;
import org.example.backend.service.CatalogItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SeriesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CatalogItemService catalogItemService;

    @Mock
    private CatalogItemMapper catalogItemMapper;

    @InjectMocks
    private SeriesController seriesController;

    
    private CatalogItem testSeries;
    private CatalogItemDto testSeriesDto;
    private List<CatalogItem> testSeriesList;
    private List<CatalogItemDto> testSeriesDtoList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(seriesController).build();
        
        testSeries = new CatalogItem();
        testSeries.setId("1");
        testSeries.setTitle("Test Series");
        testSeries.setType(ItemType.SERIES);
        testSeries.setDescription("Test series description");
        testSeries.setStartYear(2020);
        testSeries.setEndYear(2023);
        testSeries.setTags(Arrays.asList(Tag.MYSTERY.toString(), Tag.ADVENTURE.toString()));
        testSeries.setRating(8.5);
        testSeries.setCountry(Country.USA);
        testSeries.setSeasons(3);

        EmbeddedPerson director = new EmbeddedPerson();
        director.setName("Test Director");
        director.setRole(Role.DIRECTOR);

        EmbeddedPerson actor = new EmbeddedPerson();
        actor.setName("Test Actor");
        actor.setRole(Role.ACTOR);

        testSeries.setPersons(Arrays.asList(director, actor));

        testSeriesDto = new CatalogItemDto();
        testSeriesDto.setId("1");
        testSeriesDto.setTitle("Test Series");
        testSeriesDto.setType(ItemType.SERIES);
        testSeriesDto.setDescription("Test series description");
        testSeriesDto.setStartYear(2020);
        testSeriesDto.setEndYear(2023);
        testSeriesDto.setTags(Arrays.asList(Tag.MYSTERY.toString(), Tag.ADVENTURE.toString()));
        testSeriesDto.setRating(8.5);
        testSeriesDto.setCountry(Country.USA);
        testSeriesDto.setSeasons(3);

        testSeriesList = Collections.singletonList(testSeries);
        testSeriesDtoList = Collections.singletonList(testSeriesDto);

        when(catalogItemMapper.toDtoList(any())).thenReturn(testSeriesDtoList);
    }

    @Test
    @DisplayName("Should search series with filter")
    void shouldSearchSeriesWithFilter() throws Exception {
        when(catalogItemService.searchSeries(any(SeriesFilterRequest.class), anyInt(), anyInt())).thenReturn(testSeriesList);

        mockMvc.perform(get("/series")
                .param("page", "0")
                .param("size", "10")
                .param("title", "Test")
                .param("directors", "Test Director"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].title", is("Test Series")))
                .andExpect(jsonPath("$[0].type", is("SERIES")));
    }

    @Test
    @DisplayName("Should search series with empty filter")
    void shouldSearchSeriesWithEmptyFilter() throws Exception {
        when(catalogItemService.searchSeries(any(SeriesFilterRequest.class), anyInt(), anyInt())).thenReturn(testSeriesList);

        mockMvc.perform(get("/series")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].title", is("Test Series")));
    }
}