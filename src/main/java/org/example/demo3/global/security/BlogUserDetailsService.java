package org.example.demo3.global.security;

import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.user.User;
import org.example.demo3.domain.user.repository.UserRepository;
import org.example.demo3.global.exception.BlogException;
import org.example.demo3.global.exception.ErrorCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        // username은 일반적으로 로그인 시 입력한 ID
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BlogException(ErrorCode.USER_NOT_FOUND));

        return new BlogUserDetails(user);
    }
}
