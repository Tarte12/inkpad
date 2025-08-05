package org.example.demo3.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
//에러 종류, 메시지, HTTP 상태 정의
//왜 class가 아니라 enum을 쓰는 걸까?
public enum ErrorCode {

    // 🔐 회원가입 / 로그인 관련
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 유효하지 않습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 요청입니다."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 👤 유저 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    USER_ALREADY_DELETED(HttpStatus.GONE, "이미 탈퇴한 회원입니다."),
    USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "다른 사용자의 정보에는 접근할 수 없습니다."),
    USER_HAS_POSTS(HttpStatus.BAD_REQUEST, "작성한 게시글이 존재하여 탈퇴할 수 없습니다."),

    // 📝 게시글(Post) 관련
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."),
    POST_ALREADY_DELETED(HttpStatus.GONE, "이미 삭제된 게시글입니다."),
    POST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "게시글을 작성한 사용자만 수정/삭제할 수 있습니다."),
    INVALID_POST_CONTENT(HttpStatus.BAD_REQUEST, "게시글 내용이 유효하지 않습니다."),

    // 📂 파일 업로드 관련
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기가 너무 큽니다."),
    UNSUPPORTED_FILE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일이 존재하지 않습니다."),

    // ✅ 🧾 엑셀 업로드 관련
    INVALID_EXCEL_ROW(HttpStatus.BAD_REQUEST, "엑셀 행의 데이터가 유효하지 않습니다."),

    //AI 관련
    AI_COMMUNICATION_FAILED(HttpStatus.BAD_GATEWAY, "AI 응답 처리에 실패했습니다."),

    // ⚙️ 공통 / 시스템
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "요청에 대한 권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
