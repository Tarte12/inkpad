package org.example.demo3.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo3.domain.user.User;
import org.example.demo3.domain.user.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ 디버깅용 로그 출력
        System.out.println("=== JwtAuthenticationFilter 진입 ===");
        System.out.println("요청 URI: " + request.getRequestURI());
        System.out.println("HTTP Method: " + request.getMethod());
        System.out.println("Authorization: " + request.getHeader("Authorization"));

        // shouldNotFilter()에서 이미 필터 스킵 여부를 결정했으므로,
        // 이곳에서는 JWT 토큰 처리 로직만 수행합니다.

        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            try {
                Long userId = jwtTokenProvider.getUserIdFromToken(token);
                // 기존 findByUsername -> findById로 변경되었으므로 User 객체에 id 필드가 있다고 가정
                User user = userRepository.findById(userId).orElse(null);

                if (user != null) {
                    // 사용자 객체를 Principal로 사용 (UserDetails 구현체라면 더 좋음)
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user, // principal로 User 객체 자체를 넘겨줌 (User가 UserDetails 구현체라면 더 적합)
                                    null, // credentials (JWT 인증이므로 패스워드 필요 없음)
                                    List.of()
                            );
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("✅ SecurityContext에 인증 정보 설정 완료: " + user.getUsername());
                } else {
                    System.out.println("❌ DB에서 사용자 정보를 찾을 수 없습니다: userId=" + userId);
                }
            } catch (Exception e) {
                // 토큰 관련 예외 발생 시, 인증 실패 처리
                System.out.println("❌ JWT 토큰 처리 중 오류 발생: " + e.getMessage());
                SecurityContextHolder.clearContext(); // SecurityContext 비우기
            }
        } else {
            System.out.println("❌ 토큰이 없거나 유효하지 않습니다.");
        }

        filterChain.doFilter(request, response);
        System.out.println("=== JwtAuthenticationFilter 종료 ===");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7); // "Bearer " 제외
        }
        return null;
    }

    private static final List<String> NO_CHECK_URLS = List.of(
            "/api/auth/signup",
            "/api/auth/login",
            //Swagger 경로 추가
            "/swagger-ui",               // UI 진입 경로
            "/swagger-ui/",              // 정확한 경로 일치 시
            "/swagger-ui/index.html",    // 초기 로딩 페이지
            "/v3/api-docs",              // 문서 JSON
            "/v3/api-docs/swagger-config",
            "/swagger-ui/**",            // 정적 리소스들(js/css 등)
            "/v3/api-docs/**"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        boolean shouldSkip = NO_CHECK_URLS.stream().anyMatch(requestURI::startsWith);
        log.info("🔍 요청 경로: {}, 필터 스킵 여부: {}", requestURI, shouldSkip);
        return shouldSkip;
    }

}