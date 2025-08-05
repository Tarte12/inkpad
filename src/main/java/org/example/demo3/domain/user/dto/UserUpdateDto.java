package org.example.demo3.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "회원 정보 수정 DTO")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserUpdateDto {

    //ID, username은 바꿀 수 없게 할 것이기 때문에 제외
    //수정할 수도 안 할 수도 있는 필드 -> null 허용
    //다만 값이 들어오면 유효성은 만족해야 함
    @Schema(description = "사용자 PW", example = "test_PW")
    @Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
    private String password;
    @Schema(description = "사용자 닉네임", example = "test_nickname")
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하이어야 합니다.")
    private String nickname;
    @Schema(description = "사용자 email", example = "test_email")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    //필드 내용만 바꿔 주면 됨
    //Service에서 조건 분기해서 필드만 변경
}
