package org.example.demo3.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                //배포할 땐 설정 바꿔 줘야 함
                .allowedOrigins("http://localhost:3000") // React 개발 서버
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*") // Authorization 포함 모든 헤더 허용
                .exposedHeaders("Authorization") // 응답에 포함된 헤더 클라이언트가 읽을 수 있도록
                .allowCredentials(true); // 인증정보 포함 허용 (예: 쿠키, Authorization 헤더)
    }

    // 🔥 비동기 지원 설정 추가 <- 새로운 스레드풀 생성
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // TaskExecutor를 직접 생성 (Bean 등록 없이)
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setTaskDecorator(new SecurityContextTaskDecorator()); // 🔥 핵심!
        executor.setThreadNamePrefix("mvc-async-");
        executor.initialize();

        configurer.setTaskExecutor(executor);
        configurer.setDefaultTimeout(30000); // 30초 타임아웃
        log.info("✅ Spring MVC 비동기 지원 설정 완료 (SecurityContext 전파 설정됨)");
    }
}
