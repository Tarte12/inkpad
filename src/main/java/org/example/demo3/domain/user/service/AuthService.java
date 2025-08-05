package org.example.demo3.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.user.User;
import org.example.demo3.domain.user.UserRole;
import org.example.demo3.domain.user.dto.SignupRequestDto;
import org.example.demo3.domain.user.repository.UserRepository;
import org.example.demo3.global.exception.BlogException;
import org.example.demo3.global.exception.ErrorCode;
import org.example.demo3.global.security.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

//회원가입 및 로그인 구현 (인증 전용)
//회원가입 = 비밀번호 암호화, 중복 체크, 기본 권한 세팅
//로그인 = 사용자가 ID/PW 입력/비밀번호 검증/JWT 발급
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입 로직 -> 얜 User 엔티티로 안 옮기는 게 낫나?
    //ddd에서는 다 서비스에 몰지 말고 엔티티에 중요한 로직은 넣으라고 햇슨
    public void signup(SignupRequestDto dto) {
        // ✅ 소셜 로그인 여부 판단
        boolean isSocial = dto.getProvider() != null && dto.getProviderId() != null;

        if (isSocial) {
            // 🔍 소셜 유저 중복 체크
            Optional<User> existingUser = userRepository.findByProviderAndProviderId(dto.getProvider(), dto.getProviderId());
            if (existingUser.isPresent()) {
                return; // 이미 가입된 소셜 유저 → 아무 작업 안 함
            }

            // ✅ 신규 소셜 유저 자동 회원가입
            User socialUser = User.builder()
                    .username(dto.getUsername() != null ? dto.getUsername() : dto.getProvider() + "_" + dto.getProviderId())
                    .nickname(dto.getNickname() != null ? dto.getNickname() : "소셜유저")
                    .email(dto.getEmail())
                    .provider(dto.getProvider())
                    .providerId(dto.getProviderId())
                    .password("") // 소셜 유저는 패스워드 없음
                    .role(UserRole.USER)
                    .build();

            userRepository.save(socialUser);
            return;
        }

        // ✅ 일반 이메일 회원가입 처리
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BlogException(ErrorCode.DUPLICATED_USERNAME);
        }

        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
                .username(dto.getUsername())
                .password(encryptedPassword)
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .role(UserRole.USER)
                .build();

        System.out.println("User role before saving: " + user.getRole());

        userRepository.save(user);
    }

    @PostMapping("/admin/signup")
    public User signupAdmin(SignupRequestDto dto){
        User admin = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .role(UserRole.ADMIN)
                .build();

        // 저장 전 user 객체의 role 값을 확인해볼 수 있습니다.
        System.out.println("User role before saving: " + admin.getRole());

        return userRepository.save(admin);
    }

    public String login(String username, String rawPassword) {
        // 1. 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BlogException(ErrorCode.USER_NOT_FOUND));

        // 2. 소셜 로그인 사용자인 경우 로그인 거부
        if (user.getProvider() != null && user.getProviderId() != null) {
            throw new BlogException(ErrorCode.INVALID_PASSWORD); // 메시지는 커스터마이징해도 좋아
        }

        // 3. 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BlogException(ErrorCode.INVALID_PASSWORD);
        }

        // 4. JWT 토큰 생성 및 반환
        return jwtTokenProvider.createToken(user.getId(), user.getUsername());
    }
}
