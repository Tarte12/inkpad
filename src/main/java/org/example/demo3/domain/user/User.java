package org.example.demo3.domain.user;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username; //자체 회원가입용 ID
    private String password; //암호화된 비밀번호
    private String nickname;
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) //null 저장 방지
    private UserRole role; //권한 분리 위해 역할 부여

    //소셜 로그인용 필드 추가
    private String provider; //구글, 카카오, 네이버
    private String providerId; //소셜에서 제공하는 고유 사용자 ID

    //소셜 유저인지 체크
    public boolean isSocialUser(){
        return this.provider != null && !this.provider.isBlank();
    }

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
