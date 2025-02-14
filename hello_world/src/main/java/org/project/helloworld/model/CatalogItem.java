package org.project.helloworld.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "catalog_items")
public class CatalogItem {

    @Id
    private String id;

    private String title;

    private String description;

    private String type;

    private int releaseYear;

    private List<String> genres;

    private List<String> themes;

    private List<String> tags;
}
