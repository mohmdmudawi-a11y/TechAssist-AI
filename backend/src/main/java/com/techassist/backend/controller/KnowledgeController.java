package com.techassist.backend.controller;

import com.techassist.backend.dto.ArticleResponse;
import com.techassist.backend.dto.CreateArticleRequest;
import com.techassist.backend.service.KnowledgeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getAll() {
        return ResponseEntity.ok(knowledgeService.getAllArticles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(knowledgeService.getArticleById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArticleResponse>> search(@RequestParam("q") String query) {
        return ResponseEntity.ok(knowledgeService.searchArticles(query));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ArticleResponse>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(knowledgeService.getByCategory(category));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN')")
    public ResponseEntity<ArticleResponse> create(
            @Valid @RequestBody CreateArticleRequest request,
            Authentication auth) {
        return ResponseEntity.ok(knowledgeService.createArticle(request, auth.getName()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN')")
    public ResponseEntity<ArticleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateArticleRequest request) {
        return ResponseEntity.ok(knowledgeService.updateArticle(id, request));
    }
}