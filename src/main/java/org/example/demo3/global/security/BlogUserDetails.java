package org.example.demo3.global.security;

import lombok.Getter;
import org.example.demo3.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class BlogUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final String role;

    public BlogUserDetails(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null when constructing BlogUserDetails");
        }
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID is null - check user entity before constructing BlogUserDetails");
        }

        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole().name();
    }


    // ✅ 현재 로그인한 사용자의 ID를 반환
    public Long getId() {
        return this.id;
    }

    // ✅ 인증 객체에서 권한 정보를 꺼내기 위한 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + role); // ex) ROLE_ADMIN, ROLE_USER
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // 아래는 계정 상태에 대한 설정 - true로 고정
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
