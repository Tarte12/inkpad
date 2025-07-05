package org.example.demo3.domain.user.repository;

import org.example.demo3.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    //이게 그냥 db 연결 계층이 repository니까 jpa를 상속 받아서 쓰려고 이렇게 선언하는 건지?
    //long은 왜 있는 건지?
    //여기에 추가 코드를 적는다면 무엇을 적는지?
}
