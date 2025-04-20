package org.example.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDto {
    private String reviewerName;
    private String text;
    private double rating;
}
