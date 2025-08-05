package org.example.demo3.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//일단 플젝 모든 예외는 여기에서 처리함(추가하기 시러)
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ 400 Bad Request
    // @Valid 유효성 검사 실패 시 발생하는 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getStatus())
                .body(new ErrorResponse(
                        ErrorCode.INVALID_INPUT.name(),
                        errorMessage
                ));
    }

    // ✅ 사용자 정의 예외 처리 (내가 만든 BlogException 클래스)
    //ErrorCode에 등록된 status + 메시지 조합 반환
    //핵심 비즈니스 예외 처리 구간
    @ExceptionHandler(BlogException.class)
    public ResponseEntity<ErrorResponse> handleBlogException(BlogException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.name(), e.getMessage()));
    }
    // ✅ 401, 403은 Security에서 처리된다고 해서 지금 예쁘게 하려고 따로 오류 출력되게 쓸까 말까 고민


    // ✅ 500 Internal Server Error
    // 예상 못 한 예외 처리 -> 안전망 역할
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.name(), e.getMessage()));
    }
}

