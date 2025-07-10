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

    private String originalFilename;
    private String storedFilename;
    private String filePath;
    private Long size;
    private String contentType;
    private LocalDateTime uploadedAt;

    // ✅ 이미지 접근용 URL (CloudFront, Cloudflare 등 CDN 경유)
    private String url;

    // 게시글과의 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public File(String originalFilename, String storedFilename, String filePath,
                Long size, String contentType, LocalDateTime uploadedAt, String url) {
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.filePath = filePath;
        this.size = size;
        this.contentType = contentType;
        this.uploadedAt = uploadedAt;
        this.url = url;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
