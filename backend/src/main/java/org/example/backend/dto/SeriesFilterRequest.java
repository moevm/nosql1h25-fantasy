package org.example.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.model.Country;
import org.example.backend.model.Tag;

import java.util.List;

@Data
@NoArgsConstructor
public class SeriesFilterRequest {
    private String title;
    private List<Tag> tags;
    private Double ratingFrom;
    private Double ratingTo;
    private Integer startYearFrom;
    private Integer startYearTo;
    private Integer seasonsFrom;
    private Integer seasonsTo;
    private List<String> directors;
    private List<String> actors;
    private Country country;
}
