// /backend/src/test/java/org/example/backend/controller/CatalogControllerIntegrationTest.java

package org.example.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.dto.CatalogItemDto;
import org.example.backend.dto.EmbeddedPersonDto;
import org.example.backend.dto.ReviewDto;
import org.example.backend.model.*;
import org.example.backend.repository.CatalogItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class CatalogControllerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CatalogItemRepository catalogItemRepository;

    private CatalogItem testCatalogItem;
    private CatalogItemDto testCatalogItemDto;

    @BeforeEach
    void setUp() {
        // Очистка базы данных перед каждым тестом
        catalogItemRepository.deleteAll();

        // Подготовка тестовых данных
        testCatalogItem = new CatalogItem();
        testCatalogItem.setTitle("Integration Test Movie");
        testCatalogItem.setType(ItemType.FILM);
        testCatalogItem.setDescription("Integration test description");
        testCatalogItem.setStartYear(2023);
        testCatalogItem.setEndYear(2023);
        testCatalogItem.setTags(Arrays.asList(Tag.ADVENTURE, Tag.FANTASY));
        testCatalogItem.setRating(9.0);
        testCatalogItem.setCountry(Country.USA);
        testCatalogItem.setDuration(140);

        EmbeddedPerson director = new EmbeddedPerson();
        director.setName("Integration Director");
        director.setRole(Role.DIRECTOR);

        Review review = new Review();
        review.setReviewerName("Integration Reviewer");
        review.setText("Amazing movie");
        review.setRating(9.5);

        testCatalogItem.setPersons(Collections.singletonList(director));
        testCatalogItem.setReviews(Collections.singletonList(review));

        // Сохранить тестовый элемент в базу для тестов get запросов
        testCatalogItem = catalogItemRepository.save(testCatalogItem);

        // Создаем DTO для тестов post запросов
        testCatalogItemDto = new CatalogItemDto();
        testCatalogItemDto.setTitle("New Integration Test Movie");
        testCatalogItemDto.setType(ItemType.FILM);
        testCatalogItemDto.setDescription("New integration test description");
        testCatalogItemDto.setStartYear(2024);
        testCatalogItemDto.setEndYear(2024);
        testCatalogItemDto.setTags(Arrays.asList(Tag.SCIENCE_FICTION));
        testCatalogItemDto.setRating(8.7);
        testCatalogItemDto.setCountry(Country.UK);
        testCatalogItemDto.setDuration(130);

        EmbeddedPersonDto directorDto = new EmbeddedPersonDto();
        directorDto.setName("New Integration Director");
        directorDto.setRole(Role.DIRECTOR);

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewerName("New Integration Reviewer");
        reviewDto.setText("Good movie");
        reviewDto.setRating(8.5);

        testCatalogItemDto.setPersons(Collections.singletonList(directorDto));
        testCatalogItemDto.setReviews(Collections.singletonList(reviewDto));
    }

    @Test
    @DisplayName("Integration test - should create catalog item")
    void shouldCreateCatalogItem() throws Exception {
        mockMvc.perform(post("/catalog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCatalogItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Integration Test Movie")))
                .andExpect(jsonPath("$.type", is("FILM")))
                .andExpect(jsonPath("$.rating", is(8.7)))
                .andExpect(jsonPath("$.country", is("UK")));
    }

    @Test
    @DisplayName("Integration test - should find catalog item by ID")
    void shouldFindCatalogItemById() throws Exception {
        mockMvc.perform(get("/catalog/{id}", testCatalogItem.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testCatalogItem.getId())))
                .andExpect(jsonPath("$.title", is("Integration Test Movie")))
                .andExpect(jsonPath("$.type", is("FILM")))
                .andExpect(jsonPath("$.rating", is(9.0)));
    }

    @Test
    @DisplayName("Integration test - should return 404 when item not found")
    void shouldReturn404WhenItemNotFound() throws Exception {
        mockMvc.perform(get("/catalog/{id}", "nonexistent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Integration test - should get top picks")
    void shouldGetTopPicks() throws Exception {
        mockMvc.perform(get("/catalog/top-picks")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testCatalogItem.getId())))
                .andExpect(jsonPath("$[0].title", is("Integration Test Movie")));
    }

    @Test
    @DisplayName("Integration test - should get top of week")
    void shouldGetTopOfWeek() throws Exception {
        mockMvc.perform(get("/catalog/top-week")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testCatalogItem.getId())));
    }

    @Test
    @DisplayName("Integration test - should export catalog")
    void shouldExportCatalog() throws Exception {
        mockMvc.perform(get("/catalog/export"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testCatalogItem.getId())))
                .andExpect(jsonPath("$[0].title", is("Integration Test Movie")));
    }
}