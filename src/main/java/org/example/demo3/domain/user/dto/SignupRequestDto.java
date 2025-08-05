package org.example.demo3.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Schema(description = "회원가입 요청 DTO")
public class SignupRequestDto {

    //일반 회원가입 -> 근데 이거 유효성 검사 따로 빼는 게 낫나?
    //내 기억으로 유효성 검사 빼지 말고 DTO+어노테이션으로 하랫는데 이거인가?

    @Schema(description = "사용자 ID", example = "test_ID")
    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하이어야 합니다.")
    private String username; //아이디

    @Schema(description = "사용자 PW", example = "test_PW")
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password; //패스워드

    @Schema(description = "사용자 닉네임", example = "test_nickname")
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname; //닉네임

    @Schema(description = "사용자 email", example = "test_email")
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email; //이메일

    //일단 만들어만 놓고, 나중에 인증 받을 때 리팩토링할 것임
    //소셜 로그인용 -> 얜 유효성 검사 안 필요한가?
    @Schema(description = "(아직 구현 X) 소셜 로그인 제공자(구글, 카카오, 네이버), 일반 회원가입 생략 가능", example = "google")
    private String provider; //구글, 카카오, 네이버
    @Schema(description = "(아직 구현 X) 소셜 로그인 식별자, 일반 회원가입일 경우 생략 가능", example = "test_providerID")
    private String providerId; //12345678
}
