package org.example.demo3.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "카테고리별 게시글 통계 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor 
//JPQL에서는 파라미터 있는 생성자가 필요한데 이 어노테이션이 lombok이 생성자 만들게 해 줌
public class CategoryCountDto {
    @Schema(description = "카테고리명", example = "Spring")
    private String category;

    @Schema(description = "게시글 수", example = "10")
    private Long count;
}
