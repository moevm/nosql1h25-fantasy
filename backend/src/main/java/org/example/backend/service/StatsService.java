package org.example.nosql_backend.service;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.example.nosql_backend.dto.StatGroupField;
import org.example.nosql_backend.dto.StatMetric;
import org.example.nosql_backend.dto.StatsResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final MongoTemplate mongoTemplate;

    public List<StatsResult> getStatistics(StatGroupField groupBy, StatMetric metric) {
        List<AggregationOperation> pipeline = new ArrayList<>();

        if (groupBy.isUnwind()) {
            pipeline.add(Aggregation.unwind("$" + groupBy.getFieldName()));
        }

        GroupOperation baseGroup = Aggregation.group("$" + groupBy.getFieldName());
        GroupOperation groupWithMetric = metric.apply(baseGroup);
        pipeline.add(groupWithMetric);

        Aggregation aggregation = Aggregation.newAggregation(pipeline);
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "catalog_items", Document.class);
        List<Document> documents = results.getMappedResults();

        List<StatsResult> statsResults = new ArrayList<>();
        for (Document doc : documents) {
            StatsResult stat = new StatsResult();
            Object idVal = doc.get("_id");
            stat.setGroup(idVal != null ? idVal.toString() : null);
            if (doc.get("count") != null) {
                stat.setCount(doc.getInteger("count"));
            }
            if (doc.get("averageRating") != null) {
                stat.setAverageRating(doc.getDouble("averageRating"));
            }
            statsResults.add(stat);
        }
        return statsResults;
    }
}
