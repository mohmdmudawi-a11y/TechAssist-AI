package com.techassist.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Response from POST /api/ai/troubleshoot
 * Returns a numbered list of troubleshooting steps.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TroubleshootResponse {

    private List<String> steps;
    private String rawText;
}