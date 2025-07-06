package org.example.demo3.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.demo3.domain.user.User;

import org.example.demo3.domain.user.dto.UserUpdateDto;

@Getter
@NoArgsConstructor
//회원가입 시 요청받는 데이터
public class UserRequestDto {

    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하이어야 합니다.")
    private String username; //아이디
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password; //패스워드
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname; //닉네임

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email; //이메일
    //일단 만들어만 놓고, 나중에 인증 받을 때 리팩토링할 것임
}
