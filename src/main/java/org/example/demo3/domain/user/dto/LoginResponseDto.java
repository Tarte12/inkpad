package org.example.demo3.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "로그인 응답 DTO")
@Getter
@AllArgsConstructor
public class LoginResponseDto {
    @Schema(description = "JWT Access Token", example = "Bearer eyJhbGciOiJIUz...")
    private String accessToken;

}
