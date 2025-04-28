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
public class SeriesControllerIntegrationTest {

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

    private CatalogItem testSeries;

    @BeforeEach
    void setUp() {
        catalogItemRepository.deleteAll();

        testSeries = new CatalogItem();
        testSeries.setTitle("Integration Test Series");
        testSeries.setType(ItemType.SERIES);
        testSeries.setDescription("Integration series description");
        testSeries.setStartYear(2020);
        testSeries.setEndYear(2023);
        testSeries.setTags(Arrays.asList(Tag.MYSTERY.toString(), Tag.ADVENTURE.toString()));
        testSeries.setRating(8.7);
        testSeries.setCountry(Country.USA);
        testSeries.setSeasons(4);

        EmbeddedPerson director = new EmbeddedPerson();
        director.setName("Integration Director");
        director.setRole(Role.DIRECTOR);

        EmbeddedPerson actor = new EmbeddedPerson();
        actor.setName("Integration Actor");
        actor.setRole(Role.ACTOR);

        testSeries.setPersons(Arrays.asList(director, actor));

        testSeries = catalogItemRepository.save(testSeries);
    }

    @Test
    @DisplayName("Integration test - should search series")
    void shouldSearchSeries() throws Exception {
        mockMvc.perform(get("/series")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testSeries.getId())))
                .andExpect(jsonPath("$[0].title", is("Integration Test Series")))
                .andExpect(jsonPath("$[0].type", is("SERIES")));
    }

    @Test
    @DisplayName("Integration test - should filter series by title")
    void shouldFilterSeriesByTitle() throws Exception {
        mockMvc.perform(get("/series")
                .param("page", "0")
                .param("size", "10")
                .param("title", "Integration"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testSeries.getId())));
                
        mockMvc.perform(get("/series")
                .param("page", "0")
                .param("size", "10")
                .param("title", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Integration test - should filter series by seasons")
    void shouldFilterSeriesBySeasons() throws Exception {
        mockMvc.perform(get("/series")
                .param("page", "0")
                .param("size", "10")
                .param("seasonsFrom", "3")
                .param("seasonsTo", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testSeries.getId())));
                
        mockMvc.perform(get("/series")
                .param("page", "0")
                .param("size", "10")
                .param("seasonsFrom", "5")
                .param("seasonsTo", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}