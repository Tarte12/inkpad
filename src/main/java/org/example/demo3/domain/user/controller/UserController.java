package org.example.demo3.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.user.User;
import org.example.demo3.domain.user.dto.UserResponseDto;
import org.example.demo3.domain.user.dto.UserUpdateDto;
import org.example.demo3.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping //전체 조회 요청 처리(ResponseDto 사용)
    //ResponseEntity가 뭔지, findAll()이 뭔지
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<User> users = userService.findAll();
        List<UserResponseDto> response = users.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(value -> ResponseEntity.ok(new UserResponseDto(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UserUpdateDto dto) {
        userService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
