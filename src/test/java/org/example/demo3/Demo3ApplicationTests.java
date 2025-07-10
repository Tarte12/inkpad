package org.example.demo3;

import org.example.demo3.domain.file.uploader.FileUploader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
public class Demo3ApplicationTests {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public FileUploader fileUploader() {
            return Mockito.mock(FileUploader.class);
        }
    }

    @Test
    void contextLoads() {
        // 테스트는 빈 생성 여부만 확인 (contextLoads)
    }
}


