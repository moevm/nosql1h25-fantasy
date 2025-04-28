package org.example.backend.dto;

import org.example.backend.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class EnumConversionTest {

    @ParameterizedTest
    @EnumSource(ItemType.class)
    @DisplayName("ItemType enum values should be correct")
    void shouldHaveCorrectItemTypeValues(ItemType itemType) {
        assertTrue(Arrays.asList("BOOK", "FILM", "SERIES").contains(itemType.name()));
    }

    @ParameterizedTest
    @EnumSource(Tag.class)
    @DisplayName("Tag enum values should be correct")
    void shouldHaveCorrectTagValues(Tag tag) {
        assertTrue(Arrays.asList("FANTASY", "SCIENCE_FICTION", "ADVENTURE", "HORROR", "MYSTERY").contains(tag.name()));
    }

    @ParameterizedTest
    @EnumSource(Role.class)
    @DisplayName("Role enum values should be correct")
    void shouldHaveCorrectRoleValues(Role role) {
        assertTrue(Arrays.asList("AUTHOR", "DIRECTOR", "ACTOR").contains(role.name()));
    }

    @ParameterizedTest
    @EnumSource(Country.class)
    @DisplayName("Country enum values should be correct")
    void shouldHaveCorrectCountryValues(Country country) {
        assertTrue(Arrays.asList("USA", "UK", "CANADA", "FRANCE", "GERMANY", "RUSSIA", "JAPAN", "POLAND").contains(country.name()));
    }

    @Test
    @DisplayName("Should properly create CatalogItemDto")
    void shouldProperlyCreateCatalogItemDto() {
        CatalogItemDto dto = new CatalogItemDto();
        dto.setId("1");
        dto.setTitle("Test Title");
        dto.setType(ItemType.BOOK);
        dto.setDescription("Test Description");
        dto.setStartYear(2020);
        dto.setEndYear(2020);
        dto.setTags(Collections.singletonList("FANTASY"));
        dto.setRating(9.0);
        dto.setCountry(Country.USA);
        dto.setQuantityPages(300);
        
        EmbeddedPersonDto personDto = new EmbeddedPersonDto();
        personDto.setName("Test Author");
        personDto.setRole(Role.AUTHOR);
        dto.setPersons(Collections.singletonList(personDto));
        
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewerName("Test Reviewer");
        reviewDto.setText("Great book");
        reviewDto.setRating(9.5);
        dto.setReviews(Collections.singletonList(reviewDto));

        assertEquals("1", dto.getId());
        assertEquals("Test Title", dto.getTitle());
        assertEquals(ItemType.BOOK, dto.getType());
        assertEquals("Test Description", dto.getDescription());
        assertEquals(2020, dto.getStartYear());
        assertEquals(2020, dto.getEndYear());
        assertEquals(Collections.singletonList("FANTASY"), dto.getTags());
        assertEquals(9.0, dto.getRating());
        assertEquals(Country.USA, dto.getCountry());
        assertEquals(300, dto.getQuantityPages());
        
        assertEquals(1, dto.getPersons().size());
        assertEquals("Test Author", dto.getPersons().get(0).getName());
        assertEquals(Role.AUTHOR, dto.getPersons().get(0).getRole());
        
        assertEquals(1, dto.getReviews().size());
        assertEquals("Test Reviewer", dto.getReviews().get(0).getReviewerName());
        assertEquals("Great book", dto.getReviews().get(0).getText());
        assertEquals(9.5, dto.getReviews().get(0).getRating());
    }

    @Test
    @DisplayName("StatsResult should set and get values correctly")
    void shouldHandleStatsResultCorrectly() {
        StatsResult statsResult = new StatsResult();
        
        statsResult.setGroup("TEST");
        statsResult.setCount(10);
        statsResult.setAverageRating(8.5);
        
        assertEquals("TEST", statsResult.getGroup());
        assertEquals(10, statsResult.getCount());
        assertEquals(8.5, statsResult.getAverageRating());
    }

    @Test
    @DisplayName("Filter requests should work correctly")
    void shouldHandleFilterRequestsCorrectly() {
        BookFilterRequest bookFilter = new BookFilterRequest();
        bookFilter.setTitle("Test Book");
        bookFilter.setTags(Arrays.asList("FANTASY", "ADVENTURE"));
        bookFilter.setQuantityPagesFrom(100);
        bookFilter.setQuantityPagesTo(500);
        bookFilter.setAuthors(Arrays.asList("Author 1", "Author 2"));
        bookFilter.setCountry(Country.UK);
        
        assertEquals("Test Book", bookFilter.getTitle());
        assertEquals(Arrays.asList("FANTASY", "ADVENTURE"), bookFilter.getTags());
        assertEquals(Integer.valueOf(100), bookFilter.getQuantityPagesFrom());
        assertEquals(Integer.valueOf(500), bookFilter.getQuantityPagesTo());
        assertEquals(Arrays.asList("Author 1", "Author 2"), bookFilter.getAuthors());
        assertEquals(Country.UK, bookFilter.getCountry());
        
        MovieFilterRequest movieFilter = new MovieFilterRequest();
        movieFilter.setTitle("Test Movie");
        movieFilter.setDurationFrom(90);
        movieFilter.setDurationTo(180);
        movieFilter.setDirectors(Collections.singletonList("Director"));
        movieFilter.setActors(Collections.singletonList("Actor"));
        
        assertEquals("Test Movie", movieFilter.getTitle());
        assertEquals(Integer.valueOf(90), movieFilter.getDurationFrom());
        assertEquals(Integer.valueOf(180), movieFilter.getDurationTo());
        assertEquals(Collections.singletonList("Director"), movieFilter.getDirectors());
        assertEquals(Collections.singletonList("Actor"), movieFilter.getActors());
        
        SeriesFilterRequest seriesFilter = new SeriesFilterRequest();
        seriesFilter.setTitle("Test Series");
        seriesFilter.setSeasonsFrom(1);
        seriesFilter.setSeasonsTo(5);
        
        assertEquals("Test Series", seriesFilter.getTitle());
        assertEquals(Integer.valueOf(1), seriesFilter.getSeasonsFrom());
        assertEquals(Integer.valueOf(5), seriesFilter.getSeasonsTo());
    }
}