package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.BookFilterRequest;
import org.example.backend.dto.MovieFilterRequest;
import org.example.backend.dto.SeriesFilterRequest;
import org.example.backend.dto.AllFilterRequest;
import org.example.backend.model.*;
import org.example.backend.repository.CatalogItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CatalogItemService {

    private final CatalogItemRepository catalogItemRepository;
    private final MongoTemplate mongoTemplate;

    public List<CatalogItem> searchAlls(AllFilterRequest filter, int page, int size) {
        return searchCatalogItems(null, filter.getTitle(), filter.getTags(),
                filter.getRatingFrom(), filter.getRatingTo(),
                filter.getStartYearFrom(), filter.getStartYearTo(),
                filter.getCountry(),
                null, 
                page, size);
    }

    public List<CatalogItem> searchBooks(BookFilterRequest filter, int page, int size) {
        return searchCatalogItems(ItemType.BOOK, filter.getTitle(), filter.getTags(),
                filter.getRatingFrom(), filter.getRatingTo(),
                filter.getStartYearFrom(), filter.getStartYearTo(),
                filter.getCountry(),
                criteriaList -> {
                    addRangeCriteria(criteriaList, "quantityPages", filter.getQuantityPagesFrom(), filter.getQuantityPagesTo());
                    addPersonRoleCriteria(criteriaList, Role.AUTHOR, filter.getAuthors());
                },
                page, size);
    }

    public List<CatalogItem> searchMovies(MovieFilterRequest filter, int page, int size) {
        return searchCatalogItems(ItemType.FILM, filter.getTitle(), filter.getTags(),
                filter.getRatingFrom(), filter.getRatingTo(),
                filter.getStartYearFrom(), filter.getStartYearTo(),
                filter.getCountry(),
                criteriaList -> {
                    addRangeCriteria(criteriaList, "duration", filter.getDurationFrom(), filter.getDurationTo());
                    addPersonRoleCriteria(criteriaList, Role.DIRECTOR, filter.getDirectors());
                    addPersonRoleCriteria(criteriaList, Role.ACTOR, filter.getActors());
                },
                page, size);
    }

    public List<CatalogItem> searchSeries(SeriesFilterRequest filter, int page, int size) {
        return searchCatalogItems(ItemType.SERIES, filter.getTitle(), filter.getTags(),
                filter.getRatingFrom(), filter.getRatingTo(),
                filter.getStartYearFrom(), filter.getStartYearTo(),
                filter.getCountry(),
                criteriaList -> {
                    addRangeCriteria(criteriaList, "seasons", filter.getSeasonsFrom(), filter.getSeasonsTo());
                    addPersonRoleCriteria(criteriaList, Role.DIRECTOR, filter.getDirectors());
                    addPersonRoleCriteria(criteriaList, Role.ACTOR, filter.getActors());
                },
                page, size);
    }

    public Optional<CatalogItem> getItemById(String id) {
        return catalogItemRepository.findById(id);
    }

    public CatalogItem saveItem(CatalogItem item) {
        return catalogItemRepository.save(item);
    }

    public List<CatalogItem> getTopRatedItems(int limit) {
        return catalogItemRepository.findAll(
               PageRequest.of(0, limit, Sort.by("rating").descending())
        ).getContent();
    }

    public List<CatalogItem> getMostReviewedItems(int limit) {
        List<CatalogItem> all = catalogItemRepository.findAll();
        all.sort((a, b) -> {
            int countA = (a.getReviews() != null ? a.getReviews().size() : 0);
            int countB = (b.getReviews() != null ? b.getReviews().size() : 0);
            return Integer.compare(countB, countA);
        });
        if (limit >= 0 && all.size() > limit) {
            return all.subList(0, limit);
        } else {
            return all;
        }
    }

    private List<CatalogItem> searchCatalogItems(ItemType itemType,
                                                 String title, List<String> tags,
                                                 Double ratingFrom, Double ratingTo,
                                                 Integer startYearFrom, Integer startYearTo,
                                                 Country country,
                                                 Consumer<List<Criteria>> extraCriteria,
                                                 Integer page, Integer size) {
        List<Criteria> criteriaList = new ArrayList<>();

        if(itemType!=null) {
            criteriaList.add(Criteria.where("type").is(itemType));
        }

        addCommonCriteria(criteriaList, title, tags, country);
        addRangeCriteria(criteriaList, "rating", ratingFrom, ratingTo);
        addRangeCriteria(criteriaList, "startYear", startYearFrom, startYearTo);

        if (extraCriteria != null) {
            extraCriteria.accept(criteriaList);
        }

        Query query = new Query();
        if(!criteriaList.isEmpty()) {
            Criteria combinedCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
            query.addCriteria(combinedCriteria);
        }

        if (page != null && size != null && size > 0 && page >= 0) {
            query.with(PageRequest.of(page, size));
        }

        return mongoTemplate.find(query, CatalogItem.class);
    }

    private void addCommonCriteria(List<Criteria> criteriaList, String title, List<String> tags, Country country) {
        if (title != null && !title.isEmpty()) {
            criteriaList.add(Criteria.where("title").regex(title, "i"));
        }

        if (tags != null && !tags.isEmpty()) {
            criteriaList.add(Criteria.where("tags").in(tags));
        }

        if (country != null) {
            criteriaList.add(Criteria.where("country").is(country));
        }
    }

    private <T extends Comparable<T>> void addRangeCriteria(List<Criteria> criteriaList, String field, T from, T to) {
        if (from != null || to != null) {
            Criteria criteria = Criteria.where(field);
            if (from != null) {
                criteria = criteria.gte(from);
            }
            if (to != null) {
                criteria = criteria.lte(to);
            }
            criteriaList.add(criteria);
        }
    }

    private void addPersonRoleCriteria(List<Criteria> criteriaList, Role role, List<String> names) {
        if (names != null && !names.isEmpty()) {
            List<Criteria> personOrs = new ArrayList<>();
            for (String name : names) {
                personOrs.add(Criteria.where("persons")
                        .elemMatch(Criteria.where("role").is(role)
                                .and("name").regex(name, "i")));
            }
            criteriaList.add(new Criteria().orOperator(personOrs.toArray(new Criteria[0])));
        }
    }
}
