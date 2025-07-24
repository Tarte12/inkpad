package org.example.demo3.domain.notice;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공지 중요도", example = "HIGH")
public enum Importance {
    LOW, NORMAL, HIGH
}

