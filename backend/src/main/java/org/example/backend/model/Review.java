package org.example.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Review {
    private String reviewerName;
    private String text;
    private double rating;
}
