package org.example.demo3.domain.post.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.post.dto.*;
import org.example.demo3.domain.post.service.PostService;
import org.example.demo3.global.security.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Post", description = "Post CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final ObjectMapper objectMapper;

    @Operation(
            summary = "게시글 작성",
            description = "JSON 형태의 게시글 정보와 이미지 파일들을 함께 받아 게시글을 생성합니다. multipart/form-data 형식으로 요청해야 합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 형식"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Void> createPost(
            @Parameter(description = "게시글 정보(JSON 문자열)", required = true, example = "{\"title\": \"제목\", \"content\": \"내용\", \"category\": \"일반\"}")
            @RequestPart("post") String postJson,

            @Parameter(description = "업로드할 파일 리스트 (선택)", required = false)
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws Exception {
        Long userId = SecurityUtil.getCurrentUserId();
        PostRequestDto postDto = objectMapper.readValue(postJson, PostRequestDto.class);
        postService.createPost(userId, postDto, files);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "전체 게시글 조회", description = "페이지네이션 정보를 기반으로 게시글 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> findAllPaged(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(postService.findAllPaged(pageable));
    }

    @Operation(summary = "게시글 단건 조회", description = "게시글 ID를 기준으로 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> findById(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(postService.findById(id));

    }

    @Operation(summary = "게시글 수정", description = "게시글 ID에 해당하는 게시글을 수정합니다. (multipart/form-data)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long id,

            @Parameter(description = "게시글 수정 내용(JSON)", required = true)
            @Valid  @RequestPart("post") PostUpdateDto dto,

            @Parameter(description = "첨부 파일 목록", required = false)
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws Exception {
        Long userId = SecurityUtil.getCurrentUserId(); // 또는 커스텀 UserDetails에서 getId()
        postService.update(id, userId, dto, files);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "게시글 삭제", description = "게시글 ID에 해당하는 게시글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId(); // ✅ 운영용
        postService.delete(id, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "카테고리별 게시글 수 조회", description = "카테고리별 게시글 개수를 집계하여 제공합니다.")
    @ApiResponse(responseCode = "200", description = "카테고리 통계 조회 성공")
    @GetMapping("/statistics/category-count")
    public ResponseEntity<List<CategoryCountDto>> getCategoryStats() {
        return ResponseEntity.ok(postService.getPostCountByCategory());
    }

    @Operation(summary = "인기 게시글 조회", description = "조회수가 높은 인기 게시글 목록을 제공합니다.")
    @ApiResponse(responseCode = "200", description = "인기 게시글 조회 성공")
    @GetMapping("/popular")
    public ResponseEntity<List<PopularPostDto>> getPopularPosts() {
        return ResponseEntity.ok(postService.getPopularPosts());
    }

}

