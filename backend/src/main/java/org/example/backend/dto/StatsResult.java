package org.example.nosql_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatsResult {
    private String group;
    private Integer count;
    private Double averageRating;
}
