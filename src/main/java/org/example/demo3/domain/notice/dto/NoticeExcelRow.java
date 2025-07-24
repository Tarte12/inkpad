package org.example.demo3.domain.notice.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "공지사항 엑셀 업로드 행 단위 DTO")
@Getter
@Setter
@NoArgsConstructor
public class NoticeExcelRow {

    //실제 Excel 열 이름이 제목, 내용, 중요도여야 자동 매핑됨
    @ExcelProperty("제목")
    @Schema(description = "공지 제목", example = "서버 점검 안내")
    private String title;
    @Schema(description = "공지 내용", example = "2025년 8월 1일 02:00 서버 점검 예정")
    @ExcelProperty("내용")
    private String content;
    @Schema(description = "공지 중요도 (HIGH 또는 LOW)", example = "HIGH")
    @ExcelProperty("중요도")
    private String importance; // "HIGH", "NORMAL", "LOW"

    // 선택: 생성자 있어도 됨
    public NoticeExcelRow(String title, String content, String importance) {
        this.title = title;
        this.content = content;
        this.importance = importance;
    }
}

