package org.example.demo3.service;

import aj.org.objectweb.asm.commons.Remapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.user.User;
import org.example.demo3.repository.UserRepository;
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
    public  void update(Long id, String username, String email, String password){
        //수정 로직
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 없음"));
        //왜 여기에선 user 객체를 만드는 거임? 일단 db에서 해당 id에 맞는 데이터를 가져오는 명령어 같음
        //근데 orElseThrow()는 왜 있는 거임?
        user.update(id);
        //이거 그냥 User.java에 있는 update메서드 가져오겠다는 거임
    }

    public void delete(Long id){
        //삭제 로직
        userRepository.deleteById(id);
        //회원 정보 db에서 통으로 지우겠다?
    }

}
