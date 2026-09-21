package com.portfolio.app.service;

import com.portfolio.app.model.BlogPost;
import com.portfolio.app.repository.BlogPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    private final BlogPostRepository blogPostRepository;

    public BlogService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    public List<BlogPost> getPublishedArticles() {
        return blogPostRepository.findByPublishedTrueOrderByPublishedDateDesc();
    }

    public List<BlogPost> getAllArticles() {
        return blogPostRepository.findAllByOrderByPublishedDateDesc();
    }

    public Optional<BlogPost> getArticleBySlug(String slug) {
        return blogPostRepository.findBySlug(slug);
    }

    public Optional<BlogPost> getArticleById(Long id) {
        return blogPostRepository.findById(id);
    }

    public BlogPost saveArticle(BlogPost article) {
        return blogPostRepository.save(article);
    }

    public void deleteArticle(Long id) {
        blogPostRepository.deleteById(id);
    }

    @Transactional
    public void incrementViews(Long id) {
        blogPostRepository.incrementViews(id);
    }

    public long getTotalArticlesCount() {
        return blogPostRepository.count();
    }
}

