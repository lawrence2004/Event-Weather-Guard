package com.aspora.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClassificationResult {

    private String classification;
    private List<String> reasons;
    private Integer severityScore;

}
