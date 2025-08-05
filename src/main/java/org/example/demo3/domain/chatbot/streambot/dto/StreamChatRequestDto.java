package org.example.demo3.domain.chatbot.streambot.dto;

import lombok.Data;

@Data
public class StreamChatRequestDto {
    private String prompt;
    private String message; // 자유 채팅용
}
