package org.example.demo3.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "게시글 생성 요청 DTO")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostRequestDto {

    @Schema(description = "게시글 제목", example = "오늘의 개발 일지")
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @Schema(description = "게시글 본문 내용", example = "오늘은 JWT 인증 구현을 마쳤다.")
    @NotBlank(message = "본문 내용은 필수입니다.")
    private String content;

}
