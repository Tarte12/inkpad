package org.example.demo3.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.user.User;
import org.example.demo3.domain.user.dto.UserUpdateDto;
import org.example.demo3.domain.user.repository.UserRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;
    //repository에서 db 땡겨와야 해서 쓰는 코드인지?

    public User create(User user){
        //저장 로직
        return userRepository.save(user);
        //그러면 이게 jpa를 썼을 때 save 명렬어를 쓰면 user 정보를 저장하고,
        //이게 create 역할이랑 같은 거라 그냥 이 코드만 치면 되는 거임?
        //근데 왜 postservice랑 다르게 save에 빨간줄이 생기지?
    }

    public List<User> findAll(){
        //전체 조희
        //findAll()이 뭔지?
        return userRepository.findAll();
        //유저 db에서 findall = 모든 걸 조회하겠다?
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
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음"));

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
