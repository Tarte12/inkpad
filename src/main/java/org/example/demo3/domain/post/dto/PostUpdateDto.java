package org.example.demo3.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "게시글 수정 요청 DTO")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostUpdateDto {

    @Schema(description = "수정할 제목", example = "수정된 개발 일지")
    @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하로 입력해 주세요.")
    private String title;

    @Schema(description = "수정할 본문", example = "오늘은 Swagger 문서화 작업을 진행했다.")
    @Size(min = 1, max = 5000, message = "내용은 1자 이상 5000자 이하로 입력해 주세요.")
    private String content;
}
