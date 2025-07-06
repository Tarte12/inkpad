package org.example.demo3.domain.post.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.post.dto.*;
import org.example.demo3.domain.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final ObjectMapper objectMapper;

    // 1. 글 작성
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Void> createPost(
            @Valid @RequestPart("post") PostRequestDto postDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws Exception {
        Long mockUserId = 1L; // TODO: 추후 Security 인증 정보로 교체
        postService.createPost(mockUserId, postDto, files); // ✅ userId 추가
        return ResponseEntity.ok().build();
    }

    // 2. 전체 글 조회
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> findAllPaged(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(postService.findAllPaged(pageable));
    }

    // 3. 글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    // 4. 글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @Valid  @RequestPart("post") PostUpdateDto dto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws Exception {
        postService.update(id, dto, files);
        return ResponseEntity.ok().build();
    }


    // 5. 글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/statistics/category-count")
    public ResponseEntity<List<CategoryCountDto>> getCategoryStats() {
        return ResponseEntity.ok(postService.getPostCountByCategory());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PopularPostDto>> getPopularPosts() {
        return ResponseEntity.ok(postService.getPopularPosts());
    }

}

