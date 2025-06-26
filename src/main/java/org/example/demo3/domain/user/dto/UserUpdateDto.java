package org.example.demo3.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class UserUpdateDto {

    //ID, username은 바꿀 수 없게 할 것이기 때문에 제외
    private String password;
    private String nickname;
    private String email;

    //회원을 새로 만드는 게 아니고 수정이니까 toEntity() 필요 X
    //필드 내용만 바꿔 주면 됨
    //Service에서 조건 분기해서 필드만 변경
}
