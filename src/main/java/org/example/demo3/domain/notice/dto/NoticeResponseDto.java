package org.example.demo3.domain.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.demo3.domain.notice.Notice;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class NoticeResponseDto {
    private Long id;
    private String title;
    private String content;
    private String importance;
    private LocalDate publishedAt;

    public static NoticeResponseDto from(Notice notice) {
        return new NoticeResponseDto(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getImportance().name(),
                notice.getPublishedAt()
        );
    }
}


