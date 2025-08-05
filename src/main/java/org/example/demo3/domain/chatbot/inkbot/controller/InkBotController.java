package org.example.demo3.domain.chatbot.inkbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo3.domain.chatbot.inkbot.dto.CleanRequestDto;
import org.example.demo3.domain.chatbot.inkbot.dto.DraftRequestDto;
import org.example.demo3.domain.chatbot.inkbot.dto.InkBotResponseDto;
import org.example.demo3.domain.chatbot.inkbot.dto.TitleRequestDto;
import org.example.demo3.domain.chatbot.inkbot.service.InkBotService;
import org.example.demo3.global.exception.BlogException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/api/inkbot")
@RequiredArgsConstructor
@Slf4j
public class InkBotController {

    private final InkBotService inkBotService;

    @PostMapping("/draft")
    public DeferredResult<ResponseEntity<InkBotResponseDto>> draft(@RequestBody DraftRequestDto request) {
        log.info("🎯 [Controller] DeferredResult 요청 시작 - 현재 유저: {}",
                SecurityContextHolder.getContext().getAuthentication().getName());

        DeferredResult<ResponseEntity<InkBotResponseDto>> deferredResult = new DeferredResult<>(30000L);

        inkBotService.draftAsync(request)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error("❌ [DeferredResult] draft 에러 발생: {}", throwable.getMessage());
                        deferredResult.setResult(handleAsyncException(throwable));
                    } else {
                        log.info("✅ [DeferredResult] draft 성공 - 응답 설정");
                        deferredResult.setResult(ResponseEntity.ok(result));
                    }
                });

        return deferredResult;
    }

    @PostMapping("/title")
    public DeferredResult<ResponseEntity<InkBotResponseDto>> recommendTitle(@RequestBody TitleRequestDto request) {
        log.info("🎯 [Controller] DeferredResult 요청 시작 - 현재 유저: {}",
                SecurityContextHolder.getContext().getAuthentication().getName());

        DeferredResult<ResponseEntity<InkBotResponseDto>> deferredResult = new DeferredResult<>(30000L);

        inkBotService.recommendTitleAsync(request)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error("❌ [DeferredResult] title 에러 발생: {}", throwable.getMessage());
                        deferredResult.setResult(handleAsyncException(throwable));
                    } else {
                        log.info("✅ [DeferredResult] title 성공 - 응답 설정");
                        deferredResult.setResult(ResponseEntity.ok(result));
                    }
                });

        return deferredResult;
    }

    @PostMapping("/clean")
    public DeferredResult<ResponseEntity<InkBotResponseDto>> clean(@RequestBody CleanRequestDto request) {
        log.info("🎯 [Controller] DeferredResult 요청 시작 - 현재 유저: {}",
                SecurityContextHolder.getContext().getAuthentication().getName());

        DeferredResult<ResponseEntity<InkBotResponseDto>> deferredResult = new DeferredResult<>(30000L);

        inkBotService.cleanSentenceAsync(request)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error("❌ [DeferredResult] clean 에러 발생: {}", throwable.getMessage());
                        deferredResult.setResult(handleAsyncException(throwable));
                    } else {
                        log.info("✅ [DeferredResult] clean 성공 - 응답 설정");
                        deferredResult.setResult(ResponseEntity.ok(result));
                    }
                });

        return deferredResult;
    }

    private ResponseEntity<InkBotResponseDto> handleAsyncException(Throwable throwable) {
        // 로그 찍기
        throwable.printStackTrace();

        // BlogException이면 예외 코드 반영
        if (throwable.getCause() instanceof BlogException blogException) {
            return ResponseEntity
                    .status(blogException.getErrorCode().getStatus())
                    .body(new InkBotResponseDto(blogException.getMessage()));
        }

        // 그 외는 500 내부 오류로 처리
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new InkBotResponseDto("AI 처리 중 알 수 없는 오류가 발생했습니다."));
    }
}