package org.example.demo3.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.ErrorResponse;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
    private boolean success;
    private T data;
    private ErrorResponse error;

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true, data, null);
    }

    public static <T> CommonResponse<T> error(ErrorResponse error) {
        return new CommonResponse<>(false, null, error);
    }
}

