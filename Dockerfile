#베이스 이미지
FROM eclipse-temurin:21-jdk-alpine
#자바 21이 설치된 리눅스(alpine) 이미지를 베이스로 사용하겠다
#=> 내 컨테이너가 자바 21이 설치된 리눅스 환경 위에서 실행된다

WORKDIR /app
#도커 컨테이너 내부 디렉토리
#EC2에서 실행할 도커 컨테이너 안에서 /app 디렉토리에서 작업하겠다

EXPOSE 8090
#내가 사용하는 포트

# 로컬에서 만든 JAR 파일을 컨테이너 안으로 복사
COPY build/libs/demo3-0.0.1-SNAPSHOT.jar app.jar

# .env 파일 복사 추가! ✅
COPY .env .env

# Spring Boot 앱 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]