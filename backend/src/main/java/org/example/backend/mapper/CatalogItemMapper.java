package org.example.backend.mapper;

import org.example.backend.dto.CatalogItemDto;
import org.example.backend.dto.EmbeddedPersonDto;
import org.example.backend.dto.ReviewDto;
import org.example.backend.model.CatalogItem;
import org.example.backend.model.EmbeddedPerson;
import org.example.backend.model.Review;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CatalogItemMapper {
    CatalogItemDto toDto(CatalogItem item);
    CatalogItem toEntity(CatalogItemDto dto);
    EmbeddedPersonDto toDto(EmbeddedPerson person);
    EmbeddedPerson toEntity(EmbeddedPersonDto personDto);
    ReviewDto toDto(Review review);
    Review toEntity(ReviewDto reviewDto);
    List<CatalogItemDto> toDtoList(List<CatalogItem> items);
}
