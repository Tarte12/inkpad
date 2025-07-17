package org.example.demo3.domain.notice.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class NoticeExcelRow {

    private String title;
    private String content;
    private String importance; // 대문자 "HIGH" 또는 "LOW" 문자열로 들어올 것

    // 선택: 생성자 있어도 됨
    public NoticeExcelRow(String title, String content, String importance) {
        this.title = title;
        this.content = content;
        this.importance = importance;
    }
}

