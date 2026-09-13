package com.techassist.backend.repository;

import com.techassist.backend.model.KnowledgeArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KnowledgeArticleRepository extends JpaRepository<KnowledgeArticle, Long> {

    List<KnowledgeArticle> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String query);

    List<KnowledgeArticle> findByCategoryOrderByCreatedAtDesc(String category);

    List<KnowledgeArticle> findByTitleContainingIgnoreCaseOrProblemContainingIgnoreCaseOrTagsContainingIgnoreCase(
            String title, String problem, String tags);
}