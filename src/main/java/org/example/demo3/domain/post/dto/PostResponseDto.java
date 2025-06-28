package org.example.demo3.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.demo3.domain.post.Post;

@Getter
@NoArgsConstructor
//글 조회
public class PostResponseDto {

    private  Long id;
    private String title;
    private String content;
    private  String nickname; //작성자 닉네임

    //조회할 데이터를 Entity에서 받아와서 저장하는 역할
    public PostResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = post.getUser().getNickname(); // 연관관계 통해 가져옴

    }
}
