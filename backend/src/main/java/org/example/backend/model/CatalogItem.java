package org.example.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "catalog_items")
public class CatalogItem {
    @Id
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
    private List<EmbeddedPerson> persons;
    private List<Review> reviews;
}
