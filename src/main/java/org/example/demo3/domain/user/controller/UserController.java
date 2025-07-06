package org.example.demo3.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.user.User;
import org.example.demo3.domain.user.dto.UserRequestDto;
import org.example.demo3.domain.user.dto.UserResponseDto;
import org.example.demo3.domain.user.dto.UserUpdateDto;
import org.example.demo3.domain.user.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController //컨트롤러 설정
@RequiredArgsConstructor //이거 어디에 쓰는 거임?
@RequestMapping("/api/users") //엔드 포인트
public class UserController {

    private final UserService userService;
    //컨트롤러에서 서비스를 호출하는 코드인지?
    //밑줄 생기던 거 UserService 가서 @Service 어노테이션 붙여서 해결

    @PostMapping //Post HTTP 메서드와 연결(생성 요청 처리)
    //Post Http가 생성 관련 메서드? create가 생성 담당인지?
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto dto){
        User saveUser = userService.create(dto);
        return ResponseEntity.ok(new UserResponseDto(saveUser));
        //Service한테 넘기겠다는 뜻인지? create 빨간줄은 왜 생기는지

    }

    //페이징을 써서 전체 회원 목록 조회를 할 수 있게 수정
    @GetMapping("/page")
    public ResponseEntity<Page<UserResponseDto>> findAllPaged(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        Page<User> userPage = userService.findAll(pageable);
        Page<UserResponseDto> responsePage = userPage.map(UserResponseDto::new);
        return ResponseEntity.ok(responsePage);
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
