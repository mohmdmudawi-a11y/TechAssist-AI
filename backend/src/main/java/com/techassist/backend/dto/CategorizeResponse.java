package com.techassist.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response from POST /api/ai/categorize
 * The AI suggests a category, subcategory, priority and explanation.
 * The technician can accept or change it.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorizeResponse {

    private String category;
    private String subcategory;
    private String priority;
    private String reasoning;
}