package org.example.demo3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync //비동기 처리 어노테이션
@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.example.demo3.domain") // 💡 repository 경로
@EntityScan(basePackages = "org.example.demo3.domain") // 💡 entity 경로
public class Demo3Application {

    public static void main(String[] args) {
        // 🔍 환경변수 로드 테스트
        System.out.println("=== 환경변수 테스트 ===");
        System.out.println("MYSQL_HOST: " + System.getenv("MYSQL_HOST"));
        System.out.println("MYSQL_PORT: " + System.getenv("MYSQL_PORT"));
        System.out.println("MYSQL_ROOT_USERNAME: " + System.getenv("MYSQL_ROOT_USERNAME"));
        System.out.println("MYSQL_ROOT_PASSWORD: " + System.getenv("MYSQL_ROOT_PASSWORD"));
        System.out.println("========================");
        SpringApplication.run(Demo3Application.class, args);

    }

}
