package org.example.demo3.domain.notice.util;

import com.alibaba.excel.EasyExcel;
import org.example.demo3.domain.notice.dto.NoticeExcelRow;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public class NoticeExcelParser {

    public static List<NoticeExcelRow> parse(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            // EasyExcel로 파싱된 리스트 반환
            return EasyExcel.read(is)
                    .head(NoticeExcelRow.class) // 매핑할 클래스
                    .sheet()                    // 첫 번째 시트
                    .doReadSync();              // 동기 방식으로 전체 읽기
        } catch (Exception e) {
            throw new RuntimeException("공지사항 엑셀 파싱 실패: " + e.getMessage(), e);
        }
    }
}

