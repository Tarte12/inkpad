package org.example.demo3.domain.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.demo3.domain.notice.Importance;

@Schema(description = "공지사항 수정 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NoticeUpdateDto {

    @Schema(description = "공지 제목", example = "서비스 점검 안내")
    private String title;

    @Schema(description = "공지 내용", example = "7월 25일 시스템 점검 예정")
    private String content;

    @Schema(description = "중요도 (HIGH, NORMAL, LOW)", example = "HIGH")
    private Importance importance;
}
