package org.example.demo3.domain.chatbot.streambot;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo3.domain.chatbot.streambot.dto.*;
import org.example.demo3.domain.chatbot.streambot.service.StreamBotService;
import org.example.demo3.global.security.BlogUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class StreamBotWebSocketHandler extends TextWebSocketHandler {

    private final StreamBotService streamBotService;
    private final ObjectMapper objectMapper;
    private final BlogUserDetailsService userDetailsService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            StreamWebSocketMessage request = objectMapper.readValue(message.getPayload(), StreamWebSocketMessage.class);

            String username = (String) session.getAttributes().get("username");
            if (username == null) {
                sendError(session, "❌ 인증되지 않은 사용자입니다.", "auth");
                return;
            }

            // ✅ SecurityContext 수동 등록
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // ✅ 시작 메시지 전송
            sendStart(session, request.getMode());

            // ✅ 모드 분기
            switch (request.getMode()) {
                case "test" -> handleComparisonMode(session, request);
                case "live" -> handleChatMode(session, request);
                default -> sendError(session, "❌ 지원하지 않는 mode입니다.", "mode");
            }

        } catch (Exception e) {
            log.error("❌ 메시지 파싱 오류: {}", e.getMessage());
            sendError(session, "❌ 요청 형식 오류", "parse");
        }
    }

    private void handleChatMode(WebSocketSession session, StreamWebSocketMessage request) {
        try {
            StreamChatRequestDto dto = objectMapper.convertValue(request.getData(), StreamChatRequestDto.class);

            Flux<String> stream = streamBotService.chatStream(dto);

            stream.bufferTimeout(20, Duration.ofMillis(500))
                    .map(chunks -> String.join("", chunks))
                    .subscribe(
                            chunk -> sendChunk(session, chunk, "live"),
                            error -> sendError(session, "❌ 오류: " + error.getMessage(), "live"),
                            () -> sendComplete(session, "live")
                    );

        } catch (Exception e) {
            log.error("❌ chatStream 처리 오류: {}", e.getMessage());
            sendError(session, "❌ chat 처리 중 오류 발생", "live");
        }
    }

    private void handleComparisonMode(WebSocketSession session, StreamWebSocketMessage request) {
        try {
            switch (request.getType()) {
                case "draft" -> {
                    StreamDraftRequestDto dto = objectMapper.convertValue(request.getData(), StreamDraftRequestDto.class);
                    streamBotService.draftStream(dto)
                            .bufferTimeout(20, Duration.ofMillis(500))
                            .map(chunks -> String.join("", chunks))
                            .subscribe(
                                    chunk -> sendChunk(session, chunk, "comparison"),
                                    error -> sendError(session, "❌ Draft 오류: " + error.getMessage(), "comparison"),
                                    () -> sendComplete(session, "comparison")
                            );
                }
                case "title" -> {
                    StreamTitleRequestDto dto = objectMapper.convertValue(request.getData(), StreamTitleRequestDto.class);
                    streamBotService.titleStream(dto)
                            .bufferTimeout(20, Duration.ofMillis(500))
                            .map(chunks -> String.join("", chunks))
                            .subscribe(
                                    chunk -> sendChunk(session, chunk, "comparison"),
                                    error -> sendError(session, "❌ Title 오류: " + error.getMessage(), "comparison"),
                                    () -> sendComplete(session, "comparison")
                            );
                }
                case "clean" -> {
                    StreamCleanRequestDto dto = objectMapper.convertValue(request.getData(), StreamCleanRequestDto.class);
                    streamBotService.cleanStream(dto)
                            .bufferTimeout(20, Duration.ofMillis(500))
                            .map(chunks -> String.join("", chunks))
                            .subscribe(
                                    chunk -> sendChunk(session, chunk, "comparison"),
                                    error -> sendError(session, "❌ Clean 오류: " + error.getMessage(), "comparison"),
                                    () -> sendComplete(session, "comparison")
                            );
                }
                default -> sendError(session, "❌ 지원하지 않는 type입니다.", "comparison");
            }
        } catch (Exception e) {
            log.error("❌ comparison 처리 오류: {}", e.getMessage());
            sendError(session, "❌ comparison 처리 중 오류", "comparison");
        }
    }

    private void sendStart(WebSocketSession session, String mode) {
        try {
            session.sendMessage(new TextMessage(
                    objectMapper.writeValueAsString(StreamWebSocketResponse.start(mode))
            ));
        } catch (Exception e) {
            log.error("❌ start 전송 실패", e);
        }
    }

    private void sendChunk(WebSocketSession session, String chunk, String mode) {
        try {
            session.sendMessage(new TextMessage(
                    objectMapper.writeValueAsString(StreamWebSocketResponse.chunk(chunk, mode))
            ));
        } catch (Exception e) {
            log.error("❌ chunk 전송 실패", e);
        }
    }

    private void sendComplete(WebSocketSession session, String mode) {
        try {
            session.sendMessage(new TextMessage(
                    objectMapper.writeValueAsString(StreamWebSocketResponse.complete(mode))
            ));
        } catch (Exception e) {
            log.error("❌ complete 전송 실패", e);
        }
    }

    private void sendError(WebSocketSession session, String message, String mode) {
        try {
            session.sendMessage(new TextMessage(
                    objectMapper.writeValueAsString(StreamWebSocketResponse.error(message, mode))
            ));
        } catch (Exception e) {
            log.error("❌ error 전송 실패", e);
        }
    }
}