package org.example.nosql_backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.nosql_backend.dto.StatGroupField;
import org.example.nosql_backend.dto.StatMetric;
import org.example.nosql_backend.dto.StatsResult;
import org.example.nosql_backend.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatsService statsService;

    @GetMapping
    public List<StatsResult> getStatistics(@RequestParam String groupBy,
                                           @RequestParam String metric) {
        StatGroupField groupField;
        StatMetric metricType;
        try {
            groupField = StatGroupField.fromString(groupBy);
            metricType = StatMetric.fromString(metric);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return statsService.getStatistics(groupField, metricType);
    }
}
