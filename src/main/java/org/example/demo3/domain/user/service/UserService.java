package org.example.demo3.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.user.User;
import org.example.demo3.domain.user.dto.UserRequestDto;
import org.example.demo3.domain.user.dto.UserUpdateDto;
import org.example.demo3.domain.user.repository.UserRepository;
import org.example.demo3.global.exception.BlogException;
import org.example.demo3.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;
    //repository에서 db 땡겨와야 해서 쓰는 코드인지?

    public User create(UserRequestDto dto){
        User user = User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword()) // 실무라면 passwordEncoder.encode(dto.getPassword())!
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .build();

        return userRepository.save(user);
        //그러면 이게 jpa를 썼을 때 save 명렬어를 쓰면 user 정보를 저장하고,
        //이게 create 역할이랑 같은 거라 그냥 이 코드만 치면 되는 거임?
        //근데 왜 postservice랑 다르게 save에 빨간줄이 생기지?
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

    @Transactional
    public  void update(Long id, UserUpdateDto dto){
        //수정 로직
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new BlogException(ErrorCode.USER_NOT_FOUND));

        // ❗ 조건 분기: null이 아닐 때만 업데이트
        if (dto.getPassword() != null) {
            user.changePassword(dto.getPassword());
        }
        if (dto.getNickname() != null) {
            user.changeNickname(dto.getNickname());
        }
        if (dto.getEmail() != null) {
            user.changeEmail(dto.getEmail());
        }
    }


    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
