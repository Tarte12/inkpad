package org.example.demo3.global.exception;

import lombok.Getter;

@Getter
public class BlogException extends RuntimeException {

    private  final  ErrorCode errorCode;

    public BlogException(ErrorCode errorCode) {
        super(errorCode.getMessage()); //로그에 메시지가 남도록
        this.errorCode = errorCode;
    }
    public BlogException(ErrorCode errorCode, String customMessage) {
        super(customMessage); //로그에 메시지가 남도록
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
