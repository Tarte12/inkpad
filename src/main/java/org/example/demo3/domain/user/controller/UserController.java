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
import org.example.demo3.global.security.BlogUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


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

    @Operation(summary = "회원 목록 조회 (관리자)", description = "관리자가 전체 회원 목록을 페이징하여 조회")
    @PreAuthorize("hasRole('ADMIN')")
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

    @Operation(summary = "회원 검색", description = "username 또는 nickname으로 회원 검색 (일반 유저/관리자)")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 검색 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDto>> searchUsersPaged(
            @RequestParam String keyword,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        Page<User> users = userService.searchByUsernameOrNickname(keyword, pageable);
        Page<UserResponseDto> responsePage = users.map(UserResponseDto::new);
        return ResponseEntity.ok(responsePage);
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

    @Operation(summary = "본인 탈퇴", description = "로그인한 회원이 자신의 계정을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "회원 정보 없음")
    })
    @DeleteMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteMyAccount(@AuthenticationPrincipal BlogUserDetails userDetails) {
        Long userId = userDetails.getId();
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "회원 삭제 (관리자)", description = "관리자가 특정 회원을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회원 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 회원 없음")
    })
    @DeleteMapping("/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUserByAdmin(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
