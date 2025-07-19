package org.example.demo3.domain.notice.validation;

import org.example.demo3.domain.notice.dto.NoticeExcelRow;
import org.example.demo3.global.exception.BlogException;
import org.example.demo3.global.exception.ErrorCode;

import java.util.List;

public class ExcelRowValidator {

    private static final List<String> VALID_IMPORTANCE = List.of("LOW", "NORMAL", "HIGH");

    public static void validate(NoticeExcelRow row) {
        String title = row.getTitle();
        String content = row.getContent();
        String importance = row.getImportance();

        // ✅ 항목이 하나라도 비어 있으면 행은 무시하거나 예외 처리
        if (isBlank(title)) {
            throw new BlogException(ErrorCode.INVALID_EXCEL_ROW, "제목 누락 (행 제목: " + row.getTitle() + ")");
        }
        if (isBlank(content)) {
            throw new BlogException(ErrorCode.INVALID_EXCEL_ROW, "내용 누락 (행 제목: " + row.getTitle() + ")");
        }
        if (isBlank(importance)) {
            throw new BlogException(ErrorCode.INVALID_EXCEL_ROW, "중요도 누락 (행 제목: " + row.getTitle() + ")");
        }

        // ✅ 중요도 값이 존재하지만 잘못된 경우
        if (!isBlank(importance)) {
            String upperImportance = importance.trim().toUpperCase();
            if (!VALID_IMPORTANCE.contains(upperImportance)) {
                throw new BlogException(ErrorCode.INVALID_EXCEL_ROW, "잘못된 중요도");
            }
        }
    }

    private static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
