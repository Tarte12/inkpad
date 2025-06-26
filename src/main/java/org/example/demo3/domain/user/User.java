package org.example.demo3.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;

    //DB에 최종적으로 "바꾼 값"을 반영할 때 엔티티 내부에서 실행하는 메서드
    public void update(String password, String nickname, String email) {

        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }

    //회원 정보 수정을 위한 changeXXX() 메서드
    public void changePassword(String password) {
        this.password = password;
    }
    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
    public void changeEmail(String email) {
        this.email = email;
    }

}
