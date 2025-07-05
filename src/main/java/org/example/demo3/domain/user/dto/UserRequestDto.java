package org.example.demo3.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.demo3.domain.user.User;

import org.example.demo3.domain.user.dto.UserUpdateDto;

@Getter
@NoArgsConstructor
//회원가입 시 요청받는 데이터
public class UserRequestDto {

    private String username; //아이디
    private String password; //패스워드
    private String nickname; //닉네임
    private String email; //이메일
    //일단 만들어만 놓고, 나중에 인증 받을 때 리팩토링할 것임

    //DB에 저장하려면 객체여야 하므로, 받은 데이터를 객체로 만드는 메서드
    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .build();

    }
}
