package org.example.demo3.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.user.dto.LoginRequestDto;
import org.example.demo3.domain.user.dto.LoginResponseDto;
import org.example.demo3.domain.user.dto.SignupRequestDto;
import org.example.demo3.domain.user.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "회원가입 및 로그인 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "회원가입",
            description = "사용자로부터 username, password, nickname, email을 입력받아 회원가입을 수행."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 또는 중복된 사용자명"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequestDto dto) {
        authService.signup(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "관리자 회원가입",
            description = "관리자 계정으로 회원가입을 수행. 관리자 권한이 부여됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관리자 회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/admin/signup")
    public ResponseEntity<Void> signupAdmin(@RequestBody @Valid SignupRequestDto dto) {
        authService.signupAdmin(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "로그인",
            description = "사용자로부터 username과 password를 입력받아 로그인을 수행하고 JWT 토큰을 발급"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공 (JWT 토큰 반환)"),
            @ApiResponse(responseCode = "401", description = "로그인 실패 (비밀번호 불일치 또는 존재하지 않는 사용자)")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        String token = authService.login(dto.getUsername(), dto.getPassword());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}
