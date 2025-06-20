package org.example.demo3.controller;

import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.user.User;
import org.example.demo3.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //컨트롤러 설정
@RequiredArgsConstructor //이거 어디에 쓰는 거임?
@RequestMapping("/api/users") //엔드 포인트
public class UserController {

    private final UserService userService;
    //컨트롤러에서 서비스를 호출하는 코드인지?
    //밑줄 생기던 거 UserService 가서 @Service 어노테이션 붙여서 해결

    @PostMapping //Post HTTP 메서드와 연결(생성 요청 처리)
    //Post Http가 생성 관련 메서드? create가 생성 담당인지?
    public ResponseEntity<User> create(@RequestBody User user){
        return ResponseEntity.ok(userService.create(user));
        //Service한테 넘기겠다는 뜻인지? create 빨간줄은 왜 생기는지

    }

    @GetMapping //전체 조회 요청 처리
    //ResponseEntity가 뭔지, findAll()이 뭔지
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
        //ResponseEntity가 뭐 하는 애인지 그냥 감만 잡힘
        //findall로 전체를 호출하겠다는 뜻인가?

    }

    @GetMapping("/{id}")
    // 단건 조회 요청 처리 => id로 해당 id만 조회하겠다는 의미인지?
    // 그렇다면 닉네임 같은 걸로 조최하는 방법은?
    public ResponseEntity<User> findById(@PathVariable Long id){
        //findById가 뭔지, @PathVariable이 뭔지?
        return  userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        //얘는 왜 findById로 안 끝나고 뒤에 두 줄이나 붙는 거임?
    }

    @PutMapping("/{id}")
    public  ResponseEntity<Void> update(@PathVariable Long id, @RequestBody User user){
        //수정 요청 HTTP 메서드가 Put인지
        //왜 여기는 Void를 넣고, id user를 다 매개변수로 받는지?
        userService.update(id, user.getUsername(), user.getEmail(), user.getPassword());
        //이렇게 바꾸면 다 수정할 수 있는지?
        return  ResponseEntity.ok().build();
        //return 문장이 뭘 의미하는 건지?
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> delete(@PathVariable long id){
        //삭제 요청 처리
        userService.delete(id);
        return  ResponseEntity.noContent().build();
        //return문이 뭘 의미하는 건지?
    }
}
