package org.example.demo3.domain.post.repository;

import org.example.demo3.domain.post.Post;
import org.example.demo3.domain.post.dto.CategoryCountDto;
import org.example.demo3.domain.post.dto.PopularPostDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT new org.example.demo3.domain.post.dto.CategoryCountDto(p.category, COUNT(p)) " +
            "FROM Post p GROUP BY p.category")
    List<CategoryCountDto> countPostsByCategory();

    @Query("SELECT new org.example.demo3.domain.post.dto.PopularPostDto(p.id, p.title, p.views) " +
            "FROM Post p WHERE p.views > (SELECT AVG(p2.views) FROM Post p2)")
    List<PopularPostDto> findPopularPosts();


}

