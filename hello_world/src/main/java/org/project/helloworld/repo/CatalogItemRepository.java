package org.project.helloworld.repo;

import org.project.helloworld.model.CatalogItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CatalogItemRepository extends MongoRepository<CatalogItem, String> {
}
