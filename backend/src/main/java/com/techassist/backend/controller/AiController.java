package com.techassist.backend.controller;

import com.techassist.backend.dto.CategorizeRequest;
import com.techassist.backend.dto.CategorizeResponse;
import com.techassist.backend.dto.TroubleshootRequest;
import com.techassist.backend.dto.TroubleshootResponse;
import com.techassist.backend.service.AiService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * AiController — AI-powered endpoints.
 *
 *   POST /api/ai/categorize     — any authenticated user
 *   POST /api/ai/troubleshoot   — technician or admin only
 */
@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/categorize")
    public ResponseEntity<CategorizeResponse> categorize(@Valid @RequestBody CategorizeRequest request) {
        return ResponseEntity.ok(aiService.categorizeTicket(request));
    }

    @PostMapping("/troubleshoot")
    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN')")
    public ResponseEntity<TroubleshootResponse> troubleshoot(@Valid @RequestBody TroubleshootRequest request) {
        return ResponseEntity.ok(aiService.troubleshoot(request));
    }
}