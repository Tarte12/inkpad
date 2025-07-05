package org.example.demo3.domain.post.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
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
            @RequestPart("post") String postDtoJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws Exception {
        // 🔍 1. 받은 JSON 확인
        System.out.println("🔍 postDtoJson = " + postDtoJson);

        try {
            // 🔍 2. 파싱 시도
            PostRequestDto postDto = objectMapper.readValue(postDtoJson, PostRequestDto.class);

            // 🔍 3. 파싱 성공 후 실제 저장 로직 실행
            postService.createPost(postDto, files);
        } catch (Exception e) {
            // 🔍 4. 예외 전체 로그 출력 (무슨 에러인지 확인)
            System.out.println("❌ 예외 발생:");
            e.printStackTrace();  // 여기에 실제 에러 로그 나옴
            throw e;  // 에러 다시 던져서 500 응답 유지
        }

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
            @RequestPart("post") PostUpdateDto dto,
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

