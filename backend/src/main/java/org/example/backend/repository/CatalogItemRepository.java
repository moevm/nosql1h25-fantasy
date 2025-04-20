package org.example.backend.repository;

import org.example.backend.model.CatalogItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CatalogItemRepository extends MongoRepository<CatalogItem, String> {
}
