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

    private String email;

    private String password;

    //post.java보고 따라 만든 건데 builder의 역할이 뭔지 모르겠음
    //update랑 비슷하게 생겼고, builder랑 update랑 생성자랑 뭐가 다른 건데?
    //lombok이 뭔데?
    @Builder
    public User(Long id, String username, String email, String){
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void update(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
