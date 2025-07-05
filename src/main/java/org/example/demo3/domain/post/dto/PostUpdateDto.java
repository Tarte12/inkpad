package org.example.demo3.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//글 수정
public class PostUpdateDto {

    private String title;
    private String content;
}
