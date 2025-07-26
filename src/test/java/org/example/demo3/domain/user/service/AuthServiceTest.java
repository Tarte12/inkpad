package org.example.demo3.domain.user.service;

import org.example.demo3.domain.user.User;
import org.example.demo3.domain.user.dto.SignupRequestDto;
import org.example.demo3.domain.user.repository.UserRepository;
import org.example.demo3.global.exception.BlogException;
import org.example.demo3.global.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void 회원가입_성공_일반회원() {
        // given
        SignupRequestDto dto = SignupRequestDto.builder()
                .username("snowd")
                .password("1234")
                .nickname("눈사람")
                .email("snowd@example.com")
                .build();

        given(userRepository.existsByUsername("snowd")).willReturn(false);
        given(passwordEncoder.encode("1234")).willReturn("encrypted1234");

        // when
        authService.signup(dto);

        // then
        then(userRepository).should().save(argThat(user ->
                user.getUsername().equals("snowd") &&
                        user.getPassword().equals("encrypted1234") &&
                        user.getNickname().equals("눈사람")
        ));
    }

    @Test
    void 회원가입_실패_중복ID() {
        // given
        SignupRequestDto dto = SignupRequestDto.builder()
                .username("snowd")
                .password("1234")
                .build();

        given(userRepository.existsByUsername("snowd")).willReturn(true);

        // when & then
        assertThrows(BlogException.class, () -> authService.signup(dto));
    }

    @Test
    void 로그인_성공_토큰발급() {
        // given
        String rawPassword = "1234";
        String encodedPassword = "encrypted1234";
        User user = User.builder()
                .id(1L)
                .username("snowd")
                .password(encodedPassword)
                .build();

        given(userRepository.findByUsername("snowd")).willReturn(Optional.of(user));
        given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);
        given(jwtTokenProvider.createToken(1L, "snowd")).willReturn("mocked-jwt-token");

        // when
        String token = authService.login("snowd", rawPassword);

        // then
        assertEquals("mocked-jwt-token", token);
    }

    @Test
    void 로그인_실패_비밀번호틀림() {
        // given
        User user = User.builder()
                .username("snowd")
                .password("encrypted1234")
                .build();

        given(userRepository.findByUsername("snowd")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrongpass", "encrypted1234")).willReturn(false);

        // when & then
        assertThrows(BlogException.class, () -> authService.login("snowd", "wrongpass"));
    }

    @Test
    void 로그인_실패_존재하지않는유저() {
        // given
        given(userRepository.findByUsername("nouser")).willReturn(Optional.empty());

        // when & then
        assertThrows(BlogException.class, () -> authService.login("nouser", "1234"));
    }
}

