package com.techassist.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for POST /api/ai/categorize
 * Example JSON:
 * {
 *   "title": "Cannot connect to VPN",
 *   "description": "Every time I try to connect I get error 809."
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorizeRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;
}