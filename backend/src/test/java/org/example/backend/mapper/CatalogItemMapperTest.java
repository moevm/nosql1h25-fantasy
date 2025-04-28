package org.example.backend.mapper;

import org.example.backend.dto.CatalogItemDto;
import org.example.backend.dto.EmbeddedPersonDto;
import org.example.backend.dto.ReviewDto;
import org.example.backend.model.CatalogItem;
import org.example.backend.model.Country;
import org.example.backend.model.EmbeddedPerson;
import org.example.backend.model.ItemType;
import org.example.backend.model.Review;
import org.example.backend.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CatalogItemMapperTest {

    @Mock
    private CatalogItemMapper catalogItemMapper;
    
    private CatalogItem catalogItem;
    private CatalogItemDto catalogItemDto;
    private Review review;
    private ReviewDto reviewDto;
    private EmbeddedPerson person;
    private EmbeddedPersonDto personDto;

    @BeforeEach
    void setUp() {
        // Create test person data
        person = new EmbeddedPerson();
        person.setName("Christopher Nolan");
        person.setRole(Role.DIRECTOR);

        personDto = new EmbeddedPersonDto();
        personDto.setName("Christopher Nolan");
        personDto.setRole(Role.DIRECTOR);

        // Create test review data
        review = new Review();
        review.setReviewerName("John Doe");
        review.setText("Amazing movie!");
        review.setRating(5);

        reviewDto = new ReviewDto();
        reviewDto.setReviewerName("John Doe");
        reviewDto.setText("Amazing movie!");
        reviewDto.setRating(5);

        // Create test catalog item data
        catalogItem = new CatalogItem();
        catalogItem.setId("1");
        catalogItem.setTitle("Inception");
        catalogItem.setType(ItemType.FILM);
        catalogItem.setRating(8.8);
        catalogItem.setCountry(Country.USA);
        catalogItem.setStartYear(2010);
        catalogItem.setPersons(Arrays.asList(person));
        catalogItem.setReviews(Arrays.asList(review));

        catalogItemDto = new CatalogItemDto();
        catalogItemDto.setId("1");
        catalogItemDto.setTitle("Inception");
        catalogItemDto.setType(ItemType.FILM);
        catalogItemDto.setRating(8.8);
        catalogItemDto.setCountry(Country.USA);
        catalogItemDto.setStartYear(2010);
        catalogItemDto.setPersons(Arrays.asList(personDto));
        catalogItemDto.setReviews(Arrays.asList(reviewDto));
    }

    @Test
    @DisplayName("Should convert CatalogItem to CatalogItemDto")
    void shouldConvertCatalogItemToDto() {
        // Given
        when(catalogItemMapper.toDto(catalogItem)).thenReturn(catalogItemDto);
        
        // When
        CatalogItemDto result = catalogItemMapper.toDto(catalogItem);

        // Then
        assertEquals(catalogItem.getId(), result.getId());
        assertEquals(catalogItem.getTitle(), result.getTitle());
        assertEquals(catalogItem.getType(), result.getType());
        assertEquals(catalogItem.getRating(), result.getRating());
        assertEquals(catalogItem.getCountry(), result.getCountry());
        assertEquals(catalogItem.getStartYear(), result.getStartYear());
        assertEquals(catalogItem.getPersons().size(), result.getPersons().size());
        assertEquals(catalogItem.getPersons().get(0).getName(), result.getPersons().get(0).getName());
        assertEquals(catalogItem.getReviews().size(), result.getReviews().size());
        assertEquals(catalogItem.getReviews().get(0).getReviewerName(), result.getReviews().get(0).getReviewerName());
    }

    @Test
    @DisplayName("Should convert CatalogItemDto to CatalogItem")
    void shouldConvertDtoToCatalogItem() {
        // Given
        when(catalogItemMapper.toEntity(catalogItemDto)).thenReturn(catalogItem);
        
        // When
        CatalogItem result = catalogItemMapper.toEntity(catalogItemDto);

        // Then
        assertEquals(catalogItemDto.getId(), result.getId());
        assertEquals(catalogItemDto.getTitle(), result.getTitle());
        assertEquals(catalogItemDto.getType(), result.getType());
        assertEquals(catalogItemDto.getRating(), result.getRating());
        assertEquals(catalogItemDto.getCountry(), result.getCountry());
        assertEquals(catalogItemDto.getStartYear(), result.getStartYear());
        assertEquals(catalogItemDto.getPersons().size(), result.getPersons().size());
        assertEquals(catalogItemDto.getPersons().get(0).getName(), result.getPersons().get(0).getName());
        assertEquals(catalogItemDto.getReviews().size(), result.getReviews().size());
        assertEquals(catalogItemDto.getReviews().get(0).getReviewerName(), result.getReviews().get(0).getReviewerName());
    }

    @Test
    @DisplayName("Should convert list of CatalogItem to list of CatalogItemDto")
    void shouldConvertCatalogItemListToDtoList() {
        // Given
        List<CatalogItem> catalogItems = Arrays.asList(catalogItem);
        List<CatalogItemDto> expected = Arrays.asList(catalogItemDto);
        when(catalogItemMapper.toDtoList(catalogItems)).thenReturn(expected);

        // When
        List<CatalogItemDto> result = catalogItemMapper.toDtoList(catalogItems);

        // Then
        assertEquals(1, result.size());
        assertEquals(catalogItems.get(0).getId(), result.get(0).getId());
        assertEquals(catalogItems.get(0).getTitle(), result.get(0).getTitle());
    }

    @Test
    @DisplayName("Should convert Review to ReviewDto")
    void shouldConvertReviewToDto() {
        // Given
        when(catalogItemMapper.toDto(review)).thenReturn(reviewDto);
        
        // When
        ReviewDto result = catalogItemMapper.toDto(review);

        // Then
        assertEquals(review.getReviewerName(), result.getReviewerName());
        assertEquals(review.getText(), result.getText());
        assertEquals(review.getRating(), result.getRating());
    }

    @Test
    @DisplayName("Should convert ReviewDto to Review")
    void shouldConvertDtoToReview() {
        // Given
        when(catalogItemMapper.toEntity(reviewDto)).thenReturn(review);
        
        // When
        Review result = catalogItemMapper.toEntity(reviewDto);

        // Then
        assertEquals(reviewDto.getReviewerName(), result.getReviewerName());
        assertEquals(reviewDto.getText(), result.getText());
        assertEquals(reviewDto.getRating(), result.getRating());
    }

    @Test
    @DisplayName("Should convert EmbeddedPerson to EmbeddedPersonDto")
    void shouldConvertEmbeddedPersonToDto() {
        // Given
        when(catalogItemMapper.toDto(person)).thenReturn(personDto);
        
        // When
        EmbeddedPersonDto result = catalogItemMapper.toDto(person);

        // Then
        assertEquals(person.getName(), result.getName());
        assertEquals(person.getRole(), result.getRole());
    }

    @Test
    @DisplayName("Should convert EmbeddedPersonDto to EmbeddedPerson")
    void shouldConvertDtoToEmbeddedPerson() {
        // Given
        when(catalogItemMapper.toEntity(personDto)).thenReturn(person);
        
        // When
        EmbeddedPerson result = catalogItemMapper.toEntity(personDto);

        // Then
        assertEquals(personDto.getName(), result.getName());
        assertEquals(personDto.getRole(), result.getRole());
    }

    @Test
    @DisplayName("Should handle null values properly")
    void shouldHandleNullValues() {
        // Given
        when(catalogItemMapper.toDto((CatalogItem)null)).thenReturn(null);
        when(catalogItemMapper.toEntity((CatalogItemDto)null)).thenReturn(null);
        when(catalogItemMapper.toDto((Review)null)).thenReturn(null);
        when(catalogItemMapper.toEntity((ReviewDto)null)).thenReturn(null);
        when(catalogItemMapper.toDto((EmbeddedPerson)null)).thenReturn(null);
        when(catalogItemMapper.toEntity((EmbeddedPersonDto)null)).thenReturn(null);
        when(catalogItemMapper.toDtoList(null)).thenReturn(List.of());

        // When & Then
        assertNull(catalogItemMapper.toDto((CatalogItem)null));
        assertNull(catalogItemMapper.toEntity((CatalogItemDto)null));
        assertNull(catalogItemMapper.toDto((Review)null));
        assertNull(catalogItemMapper.toEntity((ReviewDto)null));
        assertNull(catalogItemMapper.toDto((EmbeddedPerson)null));
        assertNull(catalogItemMapper.toEntity((EmbeddedPersonDto)null));
        assertEquals(0, catalogItemMapper.toDtoList(null).size());
    }
} 