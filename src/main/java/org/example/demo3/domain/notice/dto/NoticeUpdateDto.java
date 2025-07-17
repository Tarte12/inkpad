package org.example.demo3.domain.notice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.demo3.domain.notice.Importance;

@Getter
@Setter
@NoArgsConstructor
public class NoticeUpdateDto {
    private String title;
    private String content;
    private Importance importance;
}
