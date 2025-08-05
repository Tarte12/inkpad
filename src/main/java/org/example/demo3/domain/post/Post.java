package org.example.demo3.domain.post;

import jakarta.persistence.*;
import lombok.*;
import org.example.demo3.domain.file.File;
import org.example.demo3.domain.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    private String category;

    private  int views;

    private LocalDateTime createdAt;

    //user랑 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //post랑 연관관계 매핑
    //post 삭제 시 연결된 file도 삭제됨(orphanRemoval = true)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files = new ArrayList<>();



    @Builder
    public Post(User user, String title, String content) {

        this.user = user;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //file 도메인과 연관관계 편의 메서드 추가
    //addFile(File File): 연관관계 설정
    public void addFile(File file) {
        files.add(file);
        file.setPost(this); // 연관관계 주도
    }
//clearFiles(): 파일 전체 삭제(Post 수정 시 사용)
    public void clearFiles() {
        for (File file : files) {
            file.setPost(null); // 양방향 정리
        }
        files.clear();
    }

}
