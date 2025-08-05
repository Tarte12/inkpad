package org.example.demo3.domain.chatbot.streambot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo3.domain.chatbot.prompt.ChatBotPromptTemplate;
import org.example.demo3.domain.chatbot.streambot.dto.StreamChatRequestDto;
import org.example.demo3.domain.chatbot.streambot.dto.StreamCleanRequestDto;
import org.example.demo3.domain.chatbot.streambot.dto.StreamDraftRequestDto;
import org.example.demo3.domain.chatbot.streambot.dto.StreamTitleRequestDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamBotService {

    private final ChatClient chatClient;

    /**
     * 📝 성능 비교용 - Draft 스트리밍 (실제 스트리밍)
     */
    public Flux<String> draftStream(StreamDraftRequestDto request) {
        long start = System.currentTimeMillis();

        String prompt = ChatBotPromptTemplate.BLOG_DRAFT.format(
                request.getTopic(),
                request.getPurpose(),
                request.getAudience(),
                request.getConstraint()
        );

        log.info("📌 [StreamBot][Draft] 시작 - topic: {}", request.getTopic());
        log.info("🧵 [StreamBot][Draft] 유저: {}", SecurityContextHolder.getContext().getAuthentication().getName());

        return chatClient.prompt()
                .user(prompt)
                .stream()
                .content()
                .doOnNext(chunk -> log.debug("📤 [Draft] Chunk: {}", chunk))
                .doOnComplete(() -> {
                    long end = System.currentTimeMillis();
                    log.info("✅ [StreamBot][Draft] 완료 - 소요 시간: {}ms", end - start);
                })
                .doOnError(error -> log.error("❌ [StreamBot][Draft] 오류: {}", error.getMessage()));
    }

    /**
     * 📰 성능 비교용 - Title 스트리밍 (실제 스트리밍)
     */
    public Flux<String> titleStream(StreamTitleRequestDto request) {
        long start = System.currentTimeMillis();

        String prompt = ChatBotPromptTemplate.RECOMMEND_TITLE.format(request.getContent());

        log.info("📌 [StreamBot][Title] 시작");
        log.info("🧵 [StreamBot][Title] 유저: {}", SecurityContextHolder.getContext().getAuthentication().getName());

        return chatClient.prompt()
                .user(prompt)
                .stream()
                .content()
                .doOnNext(chunk -> log.debug("📤 [Title] Chunk: {}", chunk))
                .doOnComplete(() -> {
                    long end = System.currentTimeMillis();
                    log.info("✅ [StreamBot][Title] 완료 - 소요 시간: {}ms", end - start);
                })
                .doOnError(error -> log.error("❌ [StreamBot][Title] 오류: {}", error.getMessage()));
    }

    /**
     * 🧹 성능 비교용 - Clean 스트리밍 (실제 스트리밍)
     */
    public Flux<String> cleanStream(StreamCleanRequestDto request) {
        long start = System.currentTimeMillis();

        String prompt = ChatBotPromptTemplate.CLEAN_SENTENCE.format(request.getSentence());

        log.info("📌 [StreamBot][Clean] 시작");
        log.info("🧵 [StreamBot][Clean] 유저: {}", SecurityContextHolder.getContext().getAuthentication().getName());

        return chatClient.prompt()
                .user(prompt)
                .stream()
                .content()
                .doOnNext(chunk -> log.debug("📤 [Clean] Chunk: {}", chunk))
                .doOnComplete(() -> {
                    long end = System.currentTimeMillis();
                    log.info("✅ [StreamBot][Clean] 완료 - 소요 시간: {}ms", end - start);
                })
                .doOnError(error -> log.error("❌ [StreamBot][Clean] 오류: {}", error.getMessage()));
    }


    /**
     * 💬 실시간 채팅용 - 자유로운 대화 (실제 스트리밍)
     */
    public Flux<String> chatStream(StreamChatRequestDto request) {
        String prompt = request.getMessage();

        log.info("📌 StreamBot chatStream() 시작 - message: {}",
                prompt.length() > 50 ? prompt.substring(0, 50) + "..." : prompt);
        log.info("🧵 [StreamBot] 현재 유저: {}",
                SecurityContextHolder.getContext().getAuthentication().getName());

        return chatClient.prompt()
                .user(prompt)
                .stream()
                .content()
                .doOnNext(chunk -> log.debug("📤 Chat Chunk: {}", chunk))
                .doOnComplete(() -> log.info("✅ StreamBot Chat 스트리밍 완료"))
                .doOnError(error -> log.error("❌ StreamBot Chat 스트리밍 오류: {}", error.getMessage()));
    }
}
