package org.example.demo3.domain.post.repository;

import org.example.demo3.domain.post.Post;
import org.example.demo3.domain.post.dto.CategoryCountDto;
import org.example.demo3.domain.post.dto.PopularPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // ✅ fetch join으로 단건 조회 (User 정보까지 함께 가져옴)
    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :id")
    Optional<Post> findWithUserById(@Param("id") Long id);

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user",
            countQuery = "SELECT COUNT(p) FROM Post p")
    Page<Post> findAllWithUserPaged(Pageable pageable);

    @Query("SELECT new org.example.demo3.domain.post.dto.CategoryCountDto(p.category, COUNT(p)) " +
            "FROM Post p GROUP BY p.category")
    List<CategoryCountDto> countPostsByCategory();

    @Query("SELECT new org.example.demo3.domain.post.dto.PopularPostDto(p.id, p.title, p.views) " +
            "FROM Post p WHERE p.views > (SELECT AVG(p2.views) FROM Post p2)")
    List<PopularPostDto> findPopularPosts();

    boolean existsByUserId(Long userId);


}

