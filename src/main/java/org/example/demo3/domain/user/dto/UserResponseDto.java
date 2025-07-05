package org.example.demo3.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.demo3.domain.user.User;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private String username;
    private String nickname;
    private String email;

    //조회할 데이터를 Entity에서 받아와서 저장하는 역할
    //생성자 쓰는 이유: 객체 생성 + 초기화 동시에 가능
    //운영자 기준으로도 password는 포함 X
    //보안 이슈, 법적 책임, 어차피 해시 처리해서 복호화 불가
    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
    }
}
