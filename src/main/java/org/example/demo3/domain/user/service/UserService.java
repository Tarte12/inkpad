package org.example.demo3.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.post.repository.PostRepository;
import org.example.demo3.domain.user.User;
import org.example.demo3.domain.user.UserRole;
import org.example.demo3.domain.user.dto.SignupRequestDto;
import org.example.demo3.domain.user.dto.UserUpdateDto;
import org.example.demo3.domain.user.repository.UserQueryRepository;
import org.example.demo3.domain.user.repository.UserRepository;
import org.example.demo3.global.exception.BlogException;
import org.example.demo3.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    //실질적으로 사용은 안 하는데 혹시 몰라서 걍 둠
    public User create(SignupRequestDto dto){
        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword())) // 실무라면 passwordEncoder.encode(dto.getPassword())!
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .role(UserRole.USER) //회원가입 시 기본 권한 USER
                .build();

        // 저장 전 user 객체의 role 값을 확인해볼 수 있습니다.
        System.out.println("User role before saving: " + user.getRole());

        return userRepository.save(user);
    }

    public User createAdmin(SignupRequestDto dto){
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

    //전체 조회를 페이징으로 할 것
    //jpa가 알아서 페이징 만들어 놔서 service에선 그냥 그거 땡겨와서 쓰는 코드만 작성
    public Page<User> findAll(Pageable pageable){
        return userRepository.findAll(pageable);

    }

    //Controller에서 findById를 가져올 수 있어야 함
    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public Page<User> searchByUsernameOrNickname(String keyword, Pageable pageable) {
        return userQueryRepository.searchByKeyword(keyword, pageable);
    }




    @Transactional
    public  void update(Long id, UserUpdateDto dto){
        //수정 로직
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new BlogException(ErrorCode.USER_NOT_FOUND));

        // ❗ 조건 분기: null이 아닐 때만 업데이트
        if (dto.getPassword() != null) {
            String encodedPw = passwordEncoder.encode(dto.getPassword());
            user.changePassword(encodedPw);
        }
        if (dto.getNickname() != null) {
            user.changeNickname(dto.getNickname());
        }
        if (dto.getEmail() != null) {
            user.changeEmail(dto.getEmail());
        }
    }

    @Transactional
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BlogException(ErrorCode.USER_NOT_FOUND));
        if (postRepository.existsByUserId(userId)) {
            throw new BlogException(ErrorCode.USER_HAS_POSTS);
        }
        userRepository.delete(user);

    }

}
