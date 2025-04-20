package org.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.model.Country;
import org.example.backend.model.ItemType;
import org.example.backend.model.Tag;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogItemDto {
    private String id;
    private String title;
    private ItemType type;
    private String description;
    private int startYear;
    private int endYear;
    private List<Tag> tags;
    private double rating;
    private Country country;
    private Integer duration;
    private Integer quantityPages;
    private Integer seasons;
    private List<EmbeddedPersonDto> persons;
    private List<ReviewDto> reviews;
}
