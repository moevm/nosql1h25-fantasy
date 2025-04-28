package org.example.backend.controller;

import org.example.backend.model.*;
import org.example.backend.repository.CatalogItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class MovieControllerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CatalogItemRepository catalogItemRepository;

    private CatalogItem testMovie;

    @BeforeEach
    void setUp() {
        catalogItemRepository.deleteAll();

        testMovie = new CatalogItem();
        testMovie.setTitle("Integration Test Movie");
        testMovie.setType(ItemType.FILM);
        testMovie.setDescription("Integration test description");
        testMovie.setStartYear(2023);
        testMovie.setEndYear(2023);
        testMovie.setTags(Arrays.asList(Tag.ADVENTURE.toString(), Tag.MYSTERY.toString()));
        testMovie.setRating(8.9);
        testMovie.setCountry(Country.USA);
        testMovie.setDuration(135);

        EmbeddedPerson director = new EmbeddedPerson();
        director.setName("Integration Director");
        director.setRole(Role.DIRECTOR);

        EmbeddedPerson actor = new EmbeddedPerson();
        actor.setName("Integration Actor");
        actor.setRole(Role.ACTOR);

        testMovie.setPersons(Arrays.asList(director, actor));

        testMovie = catalogItemRepository.save(testMovie);
    }

    @Test
    @DisplayName("Integration test - should search movies")
    void shouldSearchMovies() throws Exception {
        mockMvc.perform(get("/movies")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testMovie.getId())))
                .andExpect(jsonPath("$[0].title", is("Integration Test Movie")))
                .andExpect(jsonPath("$[0].type", is("FILM")));
    }

    @Test
    @DisplayName("Integration test - should filter movies by title")
    void shouldFilterMoviesByTitle() throws Exception {
        mockMvc.perform(get("/movies")
                .param("page", "0")
                .param("size", "10")
                .param("title", "Integration"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testMovie.getId())));
                
        mockMvc.perform(get("/movies")
                .param("page", "0")
                .param("size", "10")
                .param("title", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Integration test - should filter movies by director")
    void shouldFilterMoviesByDirector() throws Exception {
        mockMvc.perform(get("/movies")
                .param("page", "0")
                .param("size", "10")
                .param("directors", "Integration Director"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testMovie.getId())));
                
        mockMvc.perform(get("/movies")
                .param("page", "0")
                .param("size", "10")
                .param("directors", "Unknown Director"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}