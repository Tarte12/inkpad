package org.example.demo3.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "인기 게시글 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PopularPostDto {
    @Schema(description = "게시글 ID", example = "1")
    private Long id;

    @Schema(description = "게시글 제목", example = "인기 많은 글")
    private String title;

    @Schema(description = "조회수", example = "123")
    private int views;
}
