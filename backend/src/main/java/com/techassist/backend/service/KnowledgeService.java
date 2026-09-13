package com.techassist.backend.service;

import com.techassist.backend.dto.ArticleResponse;
import com.techassist.backend.dto.CreateArticleRequest;
import com.techassist.backend.model.KnowledgeArticle;
import com.techassist.backend.model.User;
import com.techassist.backend.repository.KnowledgeArticleRepository;
import com.techassist.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KnowledgeService {

    private final KnowledgeArticleRepository articleRepository;
    private final UserRepository userRepository;

    public KnowledgeService(KnowledgeArticleRepository articleRepository,
                            UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getAllArticles() {
        return articleRepository.findAll()
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ArticleResponse getArticleById(Long id) {
        KnowledgeArticle article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found: " + id));
        return toResponse(article);
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> searchArticles(String query) {
        return articleRepository
                .findByTitleContainingIgnoreCaseOrProblemContainingIgnoreCaseOrTagsContainingIgnoreCase(
                        query, query, query)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getByCategory(String category) {
        return articleRepository.findByCategoryOrderByCreatedAtDesc(category)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public ArticleResponse createArticle(CreateArticleRequest request, String authorEmail) {
        User author = userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        KnowledgeArticle article = KnowledgeArticle.builder()
                .title(request.getTitle())
                .problem(request.getProblem())
                .symptoms(request.getSymptoms())
                .steps(request.getSteps())
                .resolution(request.getResolution())
                .category(request.getCategory())
                .tags(request.getTags())
                .author(author)
                .build();

        articleRepository.save(article);
        return toResponse(article);
    }

    @Transactional
    public ArticleResponse updateArticle(Long id, CreateArticleRequest request) {
        KnowledgeArticle article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found: " + id));

        article.setTitle(request.getTitle());
        article.setProblem(request.getProblem());
        article.setSymptoms(request.getSymptoms());
        article.setSteps(request.getSteps());
        article.setResolution(request.getResolution());
        article.setCategory(request.getCategory());
        article.setTags(request.getTags());

        articleRepository.save(article);
        return toResponse(article);
    }

    private ArticleResponse toResponse(KnowledgeArticle a) {
        return ArticleResponse.builder()
                .id(a.getId())
                .title(a.getTitle())
                .problem(a.getProblem())
                .symptoms(a.getSymptoms())
                .steps(a.getSteps())
                .resolution(a.getResolution())
                .category(a.getCategory())
                .tags(a.getTags())
                .authorId(a.getAuthor() != null ? a.getAuthor().getId() : null)
                .authorName(a.getAuthor() != null
                        ? a.getAuthor().getFirstName() + " " + a.getAuthor().getLastName()
                        : null)
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}