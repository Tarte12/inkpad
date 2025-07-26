package org.example.demo3.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
}
