package com.techassist.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for POST /api/ai/troubleshoot
 * Example JSON:
 * { "problem": "User cannot access a shared folder." }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TroubleshootRequest {

    @NotBlank(message = "Problem description is required")
    private String problem;
}