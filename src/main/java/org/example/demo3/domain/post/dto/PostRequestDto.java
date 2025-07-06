package org.example.demo3.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.demo3.domain.post.Post;

@Getter
@NoArgsConstructor
//글 작성할 때 쓰는 dto
public class PostRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "본문 내용은 필수입니다.")
    private String content;

}
