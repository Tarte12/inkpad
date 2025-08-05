package org.example.demo3.domain.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.demo3.domain.notice.Notice;

import java.time.LocalDate;

@Schema(description = "공지사항 조회 응답 DTO")
@Getter
@Setter
@AllArgsConstructor
public class NoticeResponseDto {

    @Schema(description = "공지사항 ID", example = "1")
    private Long id;

    @Schema(description = "제목", example = "긴급 점검 안내")
    private String title;

    @Schema(description = "내용", example = "7월 20일 서버 점검 예정입니다.")
    private String content;

    @Schema(description = "중요도", example = "HIGH")
    private String importance;

    @Schema(description = "게시일", example = "2025-07-24", type = "string", format = "date")
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


