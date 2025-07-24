package org.example.demo3.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 요청 DTO")
@Getter
@NoArgsConstructor
public class LoginRequestDto {
    @Schema(description = "사용자 ID", example = "test_ID")
    private String username;
    @Schema(description = "사용자 PW", example = "test_PW")
    private String password;
}
