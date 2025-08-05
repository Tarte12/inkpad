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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFilename;
    private String storedFilename;
    @Column(nullable = false)
    private String filePath;
    private Long size;
    private String contentType;
    private LocalDateTime uploadedAt;

    //DB에 문자열 형태로 파일 형식 저장
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private  FileType fileType;

    // ✅ 이미지 접근용 URL (CloudFront, Cloudflare 등 CDN 경유)
    private String url;

    // 게시글과의 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public File(String originalFilename, String storedFilename, String filePath,
                Long size, String contentType, LocalDateTime uploadedAt, String url, FileType fileType) {
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.filePath = filePath;
        this.size = size;
        this.contentType = contentType;
        this.uploadedAt = uploadedAt;
        this.url = url;
        this.fileType = fileType;

    }

    public void setPost(Post post) {
        this.post = post;
    }
}
