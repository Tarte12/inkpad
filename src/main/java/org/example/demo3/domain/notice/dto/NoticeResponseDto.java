package org.example.demo3.domain.notice.dto;

import org.example.demo3.domain.notice.Notice;

import java.time.LocalDate;

public record NoticeResponseDto(
        Long id,
        String title,
        String content,
        String importance,
        LocalDate publishedAt
) {
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

