package org.example.demo3.global.security.websocket;

import jakarta.servlet.http.HttpServletRequest;
import org.example.demo3.global.security.jwt.JwtTokenProvider;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtHandshakeInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            String token = resolveToken(httpRequest);
            System.out.println("🧪 [Handshake] token: " + token);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                System.out.println("✅ [Handshake] token is valid");

                String username = jwtTokenProvider.getUsernameFromToken(token);
                System.out.println("✅ [Handshake] username from token: " + username);

                attributes.put("username", username);
                return true;
            } else {
                System.out.println("❌ [Handshake] token is invalid or null");
            }
        }

        return false;
    }


    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            System.out.println("🧪 [Token Resolver] From header: " + bearer);
            return bearer.substring(7);
        }

        String queryParamToken = request.getParameter("token");
        if (queryParamToken != null && !queryParamToken.isEmpty()) {
            System.out.println("🧪 [Token Resolver] From query param: " + queryParamToken);
            return queryParamToken;
        }

        System.out.println("❌ [Token Resolver] No token found");
        return null;
    }

        // 🛑 프론트 연동 시 아래만 남기고 위 쿼리파라미터 제거 가능
        /*
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }


        return null; */
        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, @Nullable Exception exception) {
        }

}


