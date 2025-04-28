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
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class BookControllerIntegrationTest {

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

    private CatalogItem testBook;

    @BeforeEach
    void setUp() {
        catalogItemRepository.deleteAll();

        testBook = new CatalogItem();
        testBook.setTitle("Integration Test Book");
        testBook.setType(ItemType.BOOK);
        testBook.setDescription("Integration book description");
        testBook.setStartYear(2021);
        testBook.setEndYear(2021);
        testBook.setTags(Arrays.asList(Tag.FANTASY.toString(), Tag.ADVENTURE.toString()));
        testBook.setRating(9.2);
        testBook.setCountry(Country.UK);
        testBook.setQuantityPages(420);

        EmbeddedPerson author = new EmbeddedPerson();
        author.setName("Integration Author");
        author.setRole(Role.AUTHOR);

        testBook.setPersons(Collections.singletonList(author));

        testBook = catalogItemRepository.save(testBook);
    }

    @Test
    @DisplayName("Integration test - should search books")
    void shouldSearchBooks() throws Exception {
        mockMvc.perform(get("/books")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testBook.getId())))
                .andExpect(jsonPath("$[0].title", is("Integration Test Book")))
                .andExpect(jsonPath("$[0].type", is("BOOK")));
    }

    @Test
    @DisplayName("Integration test - should filter books by title")
    void shouldFilterBooksByTitle() throws Exception {
        mockMvc.perform(get("/books")
                .param("page", "0")
                .param("size", "10")
                .param("title", "Integration"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testBook.getId())));
                
        mockMvc.perform(get("/books")
                .param("page", "0")
                .param("size", "10")
                .param("title", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Integration test - should filter books by pages")
    void shouldFilterBooksByPages() throws Exception {
        mockMvc.perform(get("/books")
                .param("page", "0")
                .param("size", "10")
                .param("quantityPagesFrom", "400")
                .param("quantityPagesTo", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testBook.getId())));
                
        mockMvc.perform(get("/books")
                .param("page", "0")
                .param("size", "10")
                .param("quantityPagesFrom", "500")
                .param("quantityPagesTo", "600"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Integration test - should filter books by author")
    void shouldFilterBooksByAuthor() throws Exception {
        mockMvc.perform(get("/books")
                .param("page", "0")
                .param("size", "10")
                .param("authors", "Integration Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testBook.getId())));
                
        mockMvc.perform(get("/books")
                .param("page", "0")
                .param("size", "10")
                .param("authors", "Unknown Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}