package org.example.demo3.domain.chatbot.streambot.dto;

import lombok.Data;

@Data
public class StreamWebSocketMessage {
    private String mode; // "comparison" (성능 비교) 또는 "chat" (실시간 채팅)
    private String type; // comparison일 때: "draft", "title", "clean" / chat일 때: "message"
    private Object data; // 요청 데이터
    private String token; // JWT 토큰
}