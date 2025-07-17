package org.example.demo3.domain.notice;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notice {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 10) // "NORMAL", "HIGH", "LOW" 모두 저장 가능
    private Importance importance;

    private LocalDate publishedAt;

    private LocalDateTime createdAt;

    @Builder
    public Notice(String title, String content, Importance importance, LocalDate publishedAt, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.importance = importance;
        this.publishedAt = publishedAt;
        this.createdAt = createdAt;
    }

    /**
     * ✅ 공지사항 수정 시 호출되는 메서드
     * - title, content, importance는 전달받은 값으로 변경
     * - publishedAt은 수정 시각(LocalDate.now())으로 갱신
     */
    public void update(String title, String content, Importance importance) {
        this.title = title;
        this.content = content;
        this.importance = importance;
        this.publishedAt = LocalDate.now(); // 수정 시 현재 날짜로 갱신
    }
}



