package org.example.backend.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StatMetricTest {

    @Test
    @DisplayName("Should convert valid string to StatMetric")
    void shouldConvertValidStringToStatMetric() {
        assertEquals(StatMetric.COUNT, StatMetric.fromString("count"));
        assertEquals(StatMetric.COUNT, StatMetric.fromString("COUNT"));
        assertEquals(StatMetric.COUNT, StatMetric.fromString("  count  "));

        assertEquals(StatMetric.AVG_RATING, StatMetric.fromString("avgrating"));
        assertEquals(StatMetric.AVG_RATING, StatMetric.fromString("averagerating"));
        assertEquals(StatMetric.AVG_RATING, StatMetric.fromString("AVGRATING"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "avg", "rating", "countavg"})
    @DisplayName("Should throw exception for invalid metric name")
    void shouldThrowExceptionForInvalidMetricName(String input) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            StatMetric.fromString(input);
        });
        assertTrue(exception.getMessage().contains("Unsupported metric parameter"));
    }

    @Test
    @DisplayName("Should throw exception for null metric value")
    void shouldThrowExceptionForNullMetricValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            StatMetric.fromString(null);
        });
        assertEquals("Metric parameter is required", exception.getMessage());
    }

    @Test
    @DisplayName("StatMetric enum should have correct values")
    void statMetricEnumShouldHaveCorrectValues() {
        StatMetric[] values = StatMetric.values();
        assertEquals(2, values.length);
        assertEquals(StatMetric.COUNT, values[0]);
        assertEquals(StatMetric.AVG_RATING, values[1]);
    }
}