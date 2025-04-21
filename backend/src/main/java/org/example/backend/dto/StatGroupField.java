package org.example.backend.dto;

import lombok.Getter;

public enum StatGroupField {
    TAGS("tags", true),
    COUNTRY("country", false),
    TYPE("type", false),
    START_YEAR("startYear", false);

    @Getter
    private final String fieldName;
    private final boolean unwindArray;

    StatGroupField(String fieldName, boolean unwindArray) {
        this.fieldName = fieldName;
        this.unwindArray = unwindArray;
    }

    public boolean isUnwind() {
        return unwindArray;
    }

    public static StatGroupField fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("GroupBy parameter is required");
        }
        String normalized = value.trim().toLowerCase();
        if (normalized.equals("tag")) {
            normalized = "tags";
        }
        for (StatGroupField field : StatGroupField.values()) {
            if (field.fieldName.equals(normalized)) {
                return field;
            }
        }
        throw new IllegalArgumentException("Unsupported groupBy parameter: " + value);
    }
}
