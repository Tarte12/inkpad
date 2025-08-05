package org.example.demo3.domain.chatbot.inkbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//사용자 요청 DTO
//record -> class 일부 라이브러리가 record 지원에 한계가 있어 class로 바꿈
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DraftRequestDto {
    private String topic;
    private String purpose;
    private String audience;
    private String constraint;
}
