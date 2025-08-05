package org.example.demo3.domain.chatbot.streambot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StreamWebSocketResponse {
    private String type; // "chunk", "complete", "error", "start"
    private String data; // 응답 데이터
    private String mode; // "comparison" 또는 "chat"

    public static StreamWebSocketResponse chunk(String chunk, String mode) {
        return new StreamWebSocketResponse("chunk", chunk, mode);
    }

    public static StreamWebSocketResponse start(String mode) {
        return new StreamWebSocketResponse("start", "응답 시작", mode);
    }

    public static StreamWebSocketResponse complete(String mode) {
        return new StreamWebSocketResponse("complete", "응답 완료", mode);
    }

    public static StreamWebSocketResponse error(String message, String mode) {
        return new StreamWebSocketResponse("error", message, mode);
    }

    // 편의 메서드
    public StreamWebSocketResponse withData(String newData) {
        return new StreamWebSocketResponse(this.type, newData, this.mode);
    }
}
