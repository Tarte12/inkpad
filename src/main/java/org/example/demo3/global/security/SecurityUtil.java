package org.example.demo3.global.security;

import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private static final Long MOCK_USER_ID = 1L;

    // profile 값을 넣어주는 setter (테스트용 주입 가능)
    private static Environment environment;

    public static void setEnvironment(Environment env) {
        environment = env;
    }

    /**
     * 현재 로그인한 사용자의 ID를 반환
     * @return Long userId
     * @throws IllegalStateException 인증 정보가 없거나 타입이 잘못된 경우
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            if (isLocalOrTestProfile()) {
                return MOCK_USER_ID;
            }
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof BlogUserDetails userDetails) {
            System.out.println("🔐 현재 사용자 ID: " + userDetails.getId());
            return userDetails.getId();
        }

        throw new IllegalStateException("인증된 사용자 정보를 가져올 수 없습니다.");
    }

    /**
     * 현재 profile이 local, test인지 확인
     */
    private static boolean isLocalOrTestProfile() {
        if (environment == null) return false; // 테스트에서 미설정 시 방지
        for (String profile : environment.getActiveProfiles()) {
            if (profile.equals("local") || profile.equals("test")) {
                return true;
            }
        }
        return false;
    }
}
