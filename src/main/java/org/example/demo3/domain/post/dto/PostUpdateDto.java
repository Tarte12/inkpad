package org.example.demo3.domain.post.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//글 수정
public class PostUpdateDto {

    @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하로 입력해 주세요.")
    private String title;
    @Size(min = 1, max = 5000, message = "내용은 1자 이상 5000자 이하로 입력해 주세요.")
    private String content;
}
