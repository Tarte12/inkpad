//package org.example.demo3.domain.user.service;
//
//import org.example.demo3.domain.user.User;
//import org.example.demo3.domain.user.dto.SignupRequestDto;
//import org.example.demo3.domain.user.dto.UserUpdateDto;
//import org.example.demo3.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserService userService;
//
//    @Test
//    void 유저_생성_성공() {
//        // given
//        SignupRequestDto dto = SignupRequestDto.builder()
//                .username("user1")
//                .password("pw1234")
//                .nickname("닉네임")
//                .email("user1@email.com")
//                .build();
//
//        given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
//
//        // when
//        User savedUser = userService.create(dto);
//
//        // then
//        assertEquals("user1", savedUser.getUsername());
//        assertEquals("닉네임", savedUser.getNickname());
//    }
//
//    @Test
//    void 유저_조회_성공() {
//        // given
//        User user = User.builder()
//                .id(1L)
//                .username("user1")
//                .build();
//
//        given(userRepository.findById(1L)).willReturn(Optional.of(user));
//
//        // when
//        Optional<User> found = userService.findById(1L);
//
//        // then
//        assertTrue(found.isPresent());
//        assertEquals("user1", found.get().getUsername());
//    }
//
//    @Test
//    void 유저_삭제_성공() {
//        // when
//        userService.delete(1L);
//
//        // then
//        then(userRepository).should().deleteById(1L);
//    }
//
//    @Test
//    void 유저_수정_성공() {
//        // given
//        User user = User.builder()
//                .id(1L)
//                .username("user1")
//                .password("old")
//                .nickname("oldNick")
//                .email("old@email.com")
//                .build();
//
//        given(userRepository.findById(1L)).willReturn(Optional.of(user));
//
//        UserUpdateDto dto = UserUpdateDto.builder()
//                .password("newPw")
//                .nickname("newNick")
//                .email("new@email.com")
//                .build();
//
//        // when
//        userService.update(1L, dto);
//
//        // then
//        assertEquals("newPw", user.getPassword());
//        assertEquals("newNick", user.getNickname());
//        assertEquals("new@email.com", user.getEmail());
//    }
//}
