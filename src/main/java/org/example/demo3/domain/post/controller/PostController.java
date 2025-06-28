package org.example.demo3.domain.post.controller;


import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.post.Post;
import org.example.demo3.domain.post.dto.PostRequestDto;
import org.example.demo3.domain.post.dto.PostResponseDto;
import org.example.demo3.domain.post.dto.PostUpdateDto;
import org.example.demo3.domain.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // 1. 글 작성
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody PostRequestDto dto) {
        postService.save(dto);
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
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody PostUpdateDto dto) {
        postService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    // 5. 글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok().build();
    }
}

