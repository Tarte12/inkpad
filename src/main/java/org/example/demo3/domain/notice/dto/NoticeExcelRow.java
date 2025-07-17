package org.example.demo3.domain.notice.dto;

import com.alibaba.excel.annotation.ExcelProperty;

public record NoticeExcelRow(

        @ExcelProperty("제목")         String title,
        @ExcelProperty("내용")         String content,
        @ExcelProperty("중요도")       String importance

) {}

