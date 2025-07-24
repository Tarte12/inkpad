package org.example.demo3.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.user.User;
import org.example.demo3.domain.user.dto.SignupRequestDto;
import org.example.demo3.domain.user.dto.UserResponseDto;
import org.example.demo3.domain.user.dto.UserUpdateDto;
import org.example.demo3.domain.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Tag(name = "User", description = "회원 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @Operation(summary = "회원 생성", description = "회원가입 정보를 입력받아 새 유저를 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 생성 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 오류")
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody SignupRequestDto dto){
        User saveUser = userService.create(dto);
        return ResponseEntity.ok(new UserResponseDto(saveUser));


    }

    @Operation(summary = "회원 목록 조회 (페이징)", description = "전체 회원 목록을 페이징하여 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 목록 조회 성공")
    })
    @GetMapping("/page")
    public ResponseEntity<Page<UserResponseDto>> findAllPaged(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        Page<User> userPage = userService.findAll(pageable);
        Page<UserResponseDto> responsePage = userPage.map(UserResponseDto::new);
        return ResponseEntity.ok(responsePage);
    }

    @Operation(summary = "회원 단건 조회", description = "ID를 기반으로 단일 회원 정보를 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 회원 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(value -> ResponseEntity.ok(new UserResponseDto(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "회원 정보 수정", description = "ID와 수정 정보를 입력받아 해당 회원 정보를 갱신")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 수정 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDto dto) {
        userService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 삭제", description = "ID를 기반으로 회원을 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회원 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 회원 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
