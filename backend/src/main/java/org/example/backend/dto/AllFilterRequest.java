package org.example.nosql_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.nosql_backend.model.Country;
import org.example.nosql_backend.model.Tag;

import java.util.List;

@Data
@NoArgsConstructor
public class AllFilterRequest {
    private String title;
    private List<Tag> tags;
    private Double ratingFrom;
    private Double ratingTo;
    private Integer startYearFrom;
    private Integer startYearTo;
    private Country country;
}
