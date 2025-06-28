package org.example.demo3.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.demo3.domain.post.Post;

@Getter
@NoArgsConstructor
//글 작성할 때 쓰는 dto
public class PostRequestDto {

    private String title;
    private String content;
    private Long userId; //PK만 잔달 받음

}
