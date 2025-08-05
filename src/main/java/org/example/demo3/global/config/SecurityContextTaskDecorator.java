package org.example.demo3.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * ✅ 비동기 스레드에 SecurityContext를 안전하게 복사하는 데코레이터
 */
@Slf4j
public class SecurityContextTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 현재 인증 정보를 복사
        SecurityContext context = SecurityContextHolder.getContext();

        return () -> {
            log.info("🧵 [데코레이터] 현재 유저: {}", context.getAuthentication() != null ? context.getAuthentication().getName() : "null");
            SecurityContext originalContext = SecurityContextHolder.getContext();
            try {
                SecurityContextHolder.setContext(context); // ✅ 인증 정보 전파
                runnable.run();                            // 원래 작업 실행
            } finally {
                SecurityContextHolder.setContext(originalContext); // ✅ 원래 컨텍스트로 복원
            }
        };
    }
}
