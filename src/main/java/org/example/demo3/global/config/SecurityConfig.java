package org.example.demo3.global.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.user.repository.UserRepository;
import org.example.demo3.global.security.BlogUserDetailsService;
import org.example.demo3.global.security.SecurityUtil;
import org.example.demo3.global.security.jwt.JwtAuthenticationFilter;
import org.example.demo3.global.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // ✅ 이 어노테이션이 필수적일 수 있습니다.
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final BlogUserDetailsService blogUserDetailsService;

    // ✅ JwtAuthenticationFilter 빈을 이 곳에 다시 정의합니다.
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        System.out.println("✅ JwtAuthenticationFilter 빈 등록됨");
        return new JwtAuthenticationFilter(jwtTokenProvider, blogUserDetailsService, userRepository); // 순서 수정!
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("✅ SecurityFilterChain 설정 시작됨"); // ← 추가
        return http
                .cors(Customizer.withDefaults()) //WebMvcConfigurer 설정 사용하도록 연결
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 🔓 누구나 접근 가능
                        .requestMatchers(
                                "/api/auth/**",              // 로그인, 회원가입
                                "/api/posts",                // 게시글 목록 조회
                                "/api/posts/{id:[0-9]+}",     // 게시글 단건 조회
                                "/api/debug/**", //디버깅 체크
                                "/notices",                  // 공지 목록 조회
                                "/swagger-ui/**",            // ✅ Swagger UI 접근 허용
                                "/v3/api-docs/**",           // ✅ OpenAPI 문서 접근 허용
                                "/swagger-resources/**",     // ✅ 기타 swagger 관련
                                "/swagger-ui.html"
                        ).permitAll()

                        // 🔐 회원/관리자만 가능
                        .requestMatchers(
                                "/api/users/search", //검색용
                                "/api/posts/**",             // 글 작성, 수정, 삭제
                                "/api/users/me/**"           // 내 정보 수정, 비밀번호 변경 등
                        ).hasAnyRole("USER", "ADMIN")

                        // 🔐 관리자만 가능
                        .requestMatchers(
                                "/api/users/page", //회원 조회
                                "/api/users/{id:[0-9]+}",
                                "/api/users/admin/users/**" ,// 회원 강퇴 포함
                                "/api/admin/**", //관리자 전용
                                "/notices/upload", //엑셀 업로드
                                "/notices/{id:[0-9]+}" //공지사항 수정 변경
                        ).hasRole("ADMIN")

                        // 🔒 그 외는 인증 필요
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private final Environment environment;

    @PostConstruct
    public void init() {
        SecurityUtil.setEnvironment(environment); // 환경 주입
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

