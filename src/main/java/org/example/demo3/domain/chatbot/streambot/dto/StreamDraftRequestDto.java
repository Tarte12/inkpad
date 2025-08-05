package org.example.demo3.domain.chatbot.streambot.dto;

import lombok.Data;

@Data
public class StreamDraftRequestDto {
    private String topic;
    private String purpose;
    private String audience;
    private String constraint;
}
