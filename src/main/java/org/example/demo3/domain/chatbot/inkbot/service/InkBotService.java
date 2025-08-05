package org.example.demo3.domain.chatbot.inkbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo3.domain.chatbot.inkbot.dto.CleanRequestDto;
import org.example.demo3.domain.chatbot.inkbot.dto.DraftRequestDto;
import org.example.demo3.domain.chatbot.inkbot.dto.InkBotResponseDto;
import org.example.demo3.domain.chatbot.inkbot.dto.TitleRequestDto;
import org.example.demo3.domain.chatbot.prompt.ChatBotPromptTemplate;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class InkBotService {

    private final ChatClient chatClient;

    @Qualifier("securityExecutor")
    private final Executor securityExecutor;

    /**
     * ✍️ 글 작성 초안 생성
     */
    public CompletableFuture<InkBotResponseDto> draftAsync(DraftRequestDto request) {
        String prompt = ChatBotPromptTemplate.BLOG_DRAFT.format(
                request.getTopic(),
                request.getPurpose(),
                request.getAudience(),
                request.getConstraint()
        );

        long start = System.currentTimeMillis();
        log.info("📌 [InkBot][Draft] 시작 - topic: {}", request.getTopic());

        return CompletableFuture.supplyAsync(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("🧵 [InkBot][Draft] 유저: {}", username);

            String response = chatClient
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            long end = System.currentTimeMillis();
            log.info("✅ [InkBot][Draft] 완료 - 소요 시간: {}ms", end - start);
            return new InkBotResponseDto(response);
        }, securityExecutor);
    }

    /**
     * 📰 제목 추천
     */
    public CompletableFuture<InkBotResponseDto> recommendTitleAsync(TitleRequestDto request) {
        String prompt = ChatBotPromptTemplate.RECOMMEND_TITLE.format(request.getContent());

        long start = System.currentTimeMillis();
        log.info("📌 [InkBot][Title] 시작");

        return CompletableFuture.supplyAsync(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("🧵 [InkBot][Title] 유저: {}", username);

            String response = chatClient
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            long end = System.currentTimeMillis();
            log.info("✅ [InkBot][Title] 완료 - 소요 시간: {}ms", end - start);
            return new InkBotResponseDto(response);
        }, securityExecutor);
    }

    /**
     * 🧹 문장 다듬기
     */
    public CompletableFuture<InkBotResponseDto> cleanSentenceAsync(CleanRequestDto request) {
        String prompt = ChatBotPromptTemplate.CLEAN_SENTENCE.format(request.getSentence());

        long start = System.currentTimeMillis();
        log.info("📌 [InkBot][Clean] 시작");

        return CompletableFuture.supplyAsync(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("🧵 [InkBot][Clean] 유저: {}", username);

            String response = chatClient
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            long end = System.currentTimeMillis();
            log.info("✅ [InkBot][Clean] 완료 - 소요 시간: {}ms", end - start);
            return new InkBotResponseDto(response);
        }, securityExecutor);
    }

}
