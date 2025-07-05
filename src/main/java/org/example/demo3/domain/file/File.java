package org.example.demo3.domain.file;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.demo3.domain.post.Post;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String originalFilename; //업로드된 이름
    private String storedFilename; //UUID 등으로 변환된 이름
    private String filePath; //파일 저장 위치 경로
    private Long size; //크기
    private String contentType; //MIME 타입
    private LocalDateTime uploadedAt; //업로드 일시

    //Post와 연관관계 매핑 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;



    public File(String originalFilename, String storedFilename, String filePath, Long size, String contentType,LocalDateTime uploadedAt){

        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.filePath = filePath;
        this.size = size;
        this.contentType = contentType;
        this.uploadedAt = uploadedAt;
    }
//file.post = post -> 외래키 설정(주인 쪽) post <-> file 연관관계 편의 메서드
    //내가 어떤 게시글(post)에 속하는지 지정
    public void setPost(Post post){
        this.post = post;
    }

}
