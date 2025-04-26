package org.example.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.model.Country;
import org.example.backend.model.Tag;

import java.util.List;

@Data
@NoArgsConstructor
public class MovieFilterRequest {
    private String title;
    private List<String> tags;
    private Double ratingFrom;
    private Double ratingTo;
    private Integer startYearFrom;
    private Integer startYearTo;
    private Integer durationFrom;
    private Integer durationTo;
    private List<String> directors;
    private List<String> actors;
    private Country country;
}
