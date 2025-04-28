package org.example.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.dto.CatalogItemDto;
import org.example.backend.dto.MovieFilterRequest;
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
public class MovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CatalogItemService catalogItemService;

    @Mock
    private CatalogItemMapper catalogItemMapper;

    @InjectMocks
    private MovieController movieController;

    private ObjectMapper objectMapper = new ObjectMapper();
    
    private CatalogItem testMovie;
    private CatalogItemDto testMovieDto;
    private List<CatalogItem> testMovies;
    private List<CatalogItemDto> testMovieDtos;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
        
        testMovie = new CatalogItem();
        testMovie.setId("1");
        testMovie.setTitle("Test Movie");
        testMovie.setType(ItemType.FILM);
        testMovie.setDescription("Test movie description");
        testMovie.setStartYear(2022);
        testMovie.setEndYear(2022);
        testMovie.setTags(Arrays.asList(Tag.MYSTERY.toString(), Tag.ADVENTURE.toString()));
        testMovie.setRating(8.7);
        testMovie.setCountry(Country.USA);
        testMovie.setDuration(120);

        EmbeddedPerson director = new EmbeddedPerson();
        director.setName("Test Director");
        director.setRole(Role.DIRECTOR);

        EmbeddedPerson actor = new EmbeddedPerson();
        actor.setName("Test Actor");
        actor.setRole(Role.ACTOR);

        testMovie.setPersons(Arrays.asList(director, actor));

        testMovieDto = new CatalogItemDto();
        testMovieDto.setId("1");
        testMovieDto.setTitle("Test Movie");
        testMovieDto.setType(ItemType.FILM);
        testMovieDto.setDescription("Test movie description");
        testMovieDto.setStartYear(2022);
        testMovieDto.setEndYear(2022);
        testMovieDto.setTags(Arrays.asList(Tag.MYSTERY.toString(), Tag.ADVENTURE.toString()));
        testMovieDto.setRating(8.7);
        testMovieDto.setCountry(Country.USA);
        testMovieDto.setDuration(120);

        testMovies = Collections.singletonList(testMovie);
        testMovieDtos = Collections.singletonList(testMovieDto);

        when(catalogItemMapper.toDtoList(any())).thenReturn(testMovieDtos);
    }

    @Test
    @DisplayName("Should search movies with filter")
    void shouldSearchMoviesWithFilter() throws Exception {
        when(catalogItemService.searchMovies(any(MovieFilterRequest.class), anyInt(), anyInt())).thenReturn(testMovies);

        mockMvc.perform(get("/movies")
                .param("page", "0")
                .param("size", "10")
                .param("title", "Test")
                .param("directors", "Test Director"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].title", is("Test Movie")))
                .andExpect(jsonPath("$[0].type", is("FILM")));
    }

    @Test
    @DisplayName("Should search movies with empty filter")
    void shouldSearchMoviesWithEmptyFilter() throws Exception {
        when(catalogItemService.searchMovies(any(MovieFilterRequest.class), anyInt(), anyInt())).thenReturn(testMovies);

        mockMvc.perform(get("/movies")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].title", is("Test Movie")));
    }
}