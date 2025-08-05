//package org.example.demo3.domain.post.service;
//
//import org.example.demo3.domain.file.repository.FileRepository;
//import org.example.demo3.domain.file.service.FileService;
//import org.example.demo3.domain.post.Post;
//import org.example.demo3.domain.post.dto.PostRequestDto;
//import org.example.demo3.domain.post.dto.PostUpdateDto;
//import org.example.demo3.domain.post.repository.PostRepository;
//import org.example.demo3.domain.user.User;
//import org.example.demo3.domain.user.repository.UserRepository;
//import org.example.demo3.global.exception.BlogException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//
//@ExtendWith(MockitoExtension.class)
//class PostServiceTest {
//
//    @Mock
//    private PostRepository postRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private FileRepository fileRepository;
//
//    @Mock
//    private FileService fileService;
//
//    @InjectMocks
//    private PostService postService;
//
//    @Test
//    void 게시글_생성_성공_파일없음() throws Exception {
//        // given
//        Long userId = 1L;
//        User user = User.builder().id(userId).username("user1").build();
//        PostRequestDto dto = PostRequestDto.builder()
//                .title("제목")
//                .content("내용")
//                .build();
//
//        given(userRepository.findById(userId)).willReturn(Optional.of(user));
//
//        // when
//        postService.createPost(userId, dto, null);
//
//        // then
//        then(postRepository).should().save(any(Post.class));
//    }
//
//    @Test
//    void 게시글_생성_실패_존재하지않는유저() {
//        // given
//        Long userId = 100L;
//        PostRequestDto dto = PostRequestDto.builder()
//                .title("제목")
//                .content("내용")
//                .build();
//
//        given(userRepository.findById(userId)).willReturn(Optional.empty());
//
//        // when & then
//        assertThrows(BlogException.class, () -> postService.createPost(userId, dto, null));
//    }
//
//    @Test
//    void 게시글_수정_성공() throws Exception {
//        // given
//        Long postId = 1L;
//        Post post = Post.builder()
//                .title("old title")
//                .content("old content")
//                .build();
//
//        //.id(1L) Builder로 주입하려 했지만, Entity에서 id는 DB에서 자동 생성
//        //따라서 Builder에 존재 X
//        //테스트 전용으로 id 필드 값 주입
//        ReflectionTestUtils.setField(post, "id", 1L);
//
//        PostUpdateDto dto = PostUpdateDto.builder()
//                .title("new title")
//                .content("new content")
//                .build();
//
//        given(postRepository.findById(postId)).willReturn(Optional.of(post));
//
//        // when
//        postService.update(postId, dto, null);
//
//        // then
//        assertEquals("new title", post.getTitle());
//        assertEquals("new content", post.getContent());
//    }
//
//    @Test
//    void 게시글_삭제_성공() {
//        // given
//        Long id = 1L;
//
//        // when
//        postService.delete(id);
//
//        // then
//        then(postRepository).should().deleteById(id);
//    }
//}
