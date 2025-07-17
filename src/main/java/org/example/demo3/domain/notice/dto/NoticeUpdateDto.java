package org.example.demo3.domain.notice.dto;

import org.example.demo3.domain.notice.Importance;

public record NoticeUpdateDto(
        String title,
        String content,
        Importance importance
) {}