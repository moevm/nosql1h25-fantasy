package org.example.nosql_backend.dto;

import org.springframework.data.mongodb.core.aggregation.GroupOperation;

public enum StatMetric {
    COUNT {
        @Override
        public GroupOperation apply(GroupOperation groupOp) {
            return groupOp.count().as("count");
        }
    },
    AVG_RATING {
        @Override
        public GroupOperation apply(GroupOperation groupOp) {
            return groupOp.avg("$rating").as("averageRating");
        }
    };

    public abstract GroupOperation apply(GroupOperation groupOp);

    public static StatMetric fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Metric parameter is required");
        }
        String val = value.trim().toLowerCase();
        if (val.equals("count")) {
            return StatMetric.COUNT;
        } else if (val.equals("avgrating") || val.equals("averagerating")) {
            return StatMetric.AVG_RATING;
        } else {
            throw new IllegalArgumentException("Unsupported metric parameter: " + value);
        }
    }
}
