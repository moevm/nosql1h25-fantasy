package org.example.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.model.Country;
import org.example.backend.model.Tag;

import java.util.List;

@Data
@NoArgsConstructor
public class BookFilterRequest {
    private String title;
    private List<String> tags;
    private Double ratingFrom;
    private Double ratingTo;
    private Integer startYearFrom;
    private Integer startYearTo;
    private Integer quantityPagesFrom;
    private Integer quantityPagesTo;
    private List<String> authors;
    private Country country;
}
