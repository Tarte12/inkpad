package org.example.demo3.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.demo3.domain.post.Post;

@Schema(description = "게시글 응답 DTO")
@Getter
@NoArgsConstructor
public class PostResponseDto {

    @Schema(description = "게시글 ID", example = "1")
    private Long id;

    @Schema(description = "게시글 제목", example = "오늘의 개발 일지")
    private String title;

    @Schema(description = "게시글 본문", example = "JWT 인증 구현 완료")
    private String content;

    @Schema(description = "작성자 닉네임", example = "dev_user")
    private String nickname;

    //조회할 데이터를 Entity에서 받아와서 저장하는 역할
    public PostResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = post.getUser().getNickname(); // 연관관계 통해 가져옴

    }
}
