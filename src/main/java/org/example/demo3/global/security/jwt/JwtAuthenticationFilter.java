package org.example.demo3.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo3.domain.user.repository.UserRepository;
import org.example.demo3.global.security.BlogUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final BlogUserDetailsService blogUserDetailsService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("=== JwtAuthenticationFilter 진입 ===");
        System.out.println("요청 URI: " + request.getRequestURI());
        System.out.println("HTTP Method: " + request.getMethod());
        System.out.println("Authorization: " + request.getHeader("Authorization"));

        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            try {
                String username = jwtTokenProvider.getUsernameFromToken(token); // ⬅️ 이 메서드 필요!
                UserDetails userDetails = blogUserDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("✅ SecurityContext에 인증 정보 설정 완료: " + username);
            } catch (Exception e) {
                System.out.println("❌ JWT 토큰 처리 중 오류 발생: " + e.getMessage());
                SecurityContextHolder.clearContext();
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
            "/v3/api-docs/**",
            "/api/debug",            // 디버깅용
            "/api/debug/",           //
            "/api/debug/**",
            "/ws/**" //웹소켓 연결
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        boolean shouldSkip = NO_CHECK_URLS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
        log.info("🔍 요청 경로: {}, 필터 스킵 여부: {}", requestURI, shouldSkip);
        return shouldSkip;
    }

}