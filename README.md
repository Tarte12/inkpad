# 📝 Inkpad: 블로그 플랫폼

Spring Boot 기반의 **블로그 플랫폼**입니다.  
단순 CRUD 구현을 넘어서, **성능 실험 → 개선 → 리팩토링 사이클**을 통해 **수치 기반 성능 향상**에 집중한 백엔드 프로젝트입니다.


## 🗂 프로젝트 아키텍처 (예정)

📌 전체 구조 흐름도: [`docs/architecture.png`](docs/architecture.png) *(추후 삽입 예정)*  
예시:

```plaintext
[Client] 
   ↓
[Spring Boot API Server]
   ├── JWT 인증 (Spring Security)
   ├── RESTful API
   ├── Excel/이미지 처리
   └── AI 응답 비동기 처리
        ↓
[DB: MySQL (RDS)]
        ↓
[S3 or Local 저장소]
        ↓
[CloudFront or Direct Response]
```


## 🗃️ ERD (예정)

## 🧱 도메인 설계 (DDD 기반)

- `User`: 회원 인증/인가, JWT 토큰 발급
- `Post`: 게시글 등록, 수정, 삭제 + 파일 업로드 연동
- `File`: 전략 기반 업로드(Local, S3), WebP 변환
- `Notice`: Excel 업로드 기반 대량 등록 처리
- `InkBot`: Spring AI + Prompt 기반 글 초안 생성 (비동기 처리)


## ⚙ 사용 기술 스택

- **Language**: Java 21
- **Framework**: Spring Boot 3, JPA, QueryDSL, Spring Security
- **Database**: MySQL (AWS RDS)
- **Infra**: Docker, AWS EC2, S3, CloudFront, Nginx
- **AI & Util**: Spring AI, Swagger, JUnit5, k6


## 🚀 주요 기능 요약

### 🔧 주요 구현 기능

- ✅ 사용자 인증/인가 (**JWT + Spring Security**)
- ✅ 게시글 CRUD + **첨부파일 업로드/삭제**
- ✅ **Excel 형식 공지사항 대량 업로드** (Batch Insert)
- ✅ 이미지 업로드 최적화 (**리사이징 + WebP 변환**)
- ✅ **Local / S3 업로드 전략 패턴** 적용
- ✅ **Spring AI 기반 챗봇**: 글 초안 자동 작성 (비동기 처리 + Prompt 템플릿)
- ✅ Swagger 기반 **API 명세화** → 프론트 없이 테스트 가능
- 🚀 EC2 + Docker + Nginx 배포 (진행 중)


## 📈 성능 개선 사례 (수치 기반 실험)

### 1️⃣ 이미지 업로드 최적화
- 원본 JPEG/PNG 업로드 시 전송량 & 응답 속도 문제 발생
- ✅ WebP 변환 + 리사이징 적용
- ✅ 부하 테스트 도구 `k6`로 성능 실험
- 🎯 **1.27s → 448ms (64.7% 개선)** / **TPS 3.6 → 10.7**

### 2️⃣ Excel 공지사항 대량 업로드
- ✅ JPA 단건 저장 → JPA Batch로 전환
- 🎯 **평균 75% 이상 성능 개선**

### 3️⃣ 파일 저장소 성능 비교 (Local vs S3)
- 🎯 S3 평균 응답 619ms / Local 대비 +6ms 향상  
  TPS 7.29 → **7.5 (+0.21)**

### 4️⃣ AI 챗봇 비동기 구조 인증 문제 해결
- ✅ `@Async` 인증 문제 → `SecurityContext` 전파 구조 개선  
- 🎯 **403 오류 해결 + 비동기 인증 유지 성공**




