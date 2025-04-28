package org.example.backend.controller;

import org.example.backend.dto.BookFilterRequest;
import org.example.backend.dto.CatalogItemDto;
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
public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CatalogItemService catalogItemService;

    @Mock
    private CatalogItemMapper catalogItemMapper;

    @InjectMocks
    private BookController bookController;
    
    private CatalogItem testBook;
    private CatalogItemDto testBookDto;
    private List<CatalogItem> testBooks;
    private List<CatalogItemDto> testBookDtos;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        
        testBook = new CatalogItem();
        testBook.setId("1");
        testBook.setTitle("Test Book");
        testBook.setType(ItemType.BOOK);
        testBook.setDescription("Test book description");
        testBook.setStartYear(2022);
        testBook.setEndYear(2022);
        testBook.setTags(Arrays.asList(Tag.FANTASY.toString(), Tag.ADVENTURE.toString()));
        testBook.setRating(9.0);
        testBook.setCountry(Country.UK);
        testBook.setQuantityPages(350);

        EmbeddedPerson author = new EmbeddedPerson();
        author.setName("Test Author");
        author.setRole(Role.AUTHOR);

        testBook.setPersons(Collections.singletonList(author));

        testBookDto = new CatalogItemDto();
        testBookDto.setId("1");
        testBookDto.setTitle("Test Book");
        testBookDto.setType(ItemType.BOOK);
        testBookDto.setDescription("Test book description");
        testBookDto.setStartYear(2022);
        testBookDto.setEndYear(2022);
        testBookDto.setTags(Arrays.asList(Tag.FANTASY.toString(), Tag.ADVENTURE.toString()));
        testBookDto.setRating(9.0);
        testBookDto.setCountry(Country.UK);
        testBookDto.setQuantityPages(350);

        testBooks = Collections.singletonList(testBook);
        testBookDtos = Collections.singletonList(testBookDto);

        when(catalogItemMapper.toDtoList(any())).thenReturn(testBookDtos);
    }

    @Test
    @DisplayName("Should search books with filter")
    void shouldSearchBooksWithFilter() throws Exception {
        when(catalogItemService.searchBooks(any(BookFilterRequest.class), anyInt(), anyInt())).thenReturn(testBooks);

        mockMvc.perform(get("/books")
                .param("page", "0")
                .param("size", "10")
                .param("title", "Test")
                .param("authors", "Test Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].title", is("Test Book")))
                .andExpect(jsonPath("$[0].type", is("BOOK")));
    }

    @Test
    @DisplayName("Should search books with empty filter")
    void shouldSearchBooksWithEmptyFilter() throws Exception {
        when(catalogItemService.searchBooks(any(BookFilterRequest.class), anyInt(), anyInt())).thenReturn(testBooks);

        mockMvc.perform(get("/books")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].title", is("Test Book")));
    }
}