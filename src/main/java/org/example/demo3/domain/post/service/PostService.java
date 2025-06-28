package org.example.demo3.domain.post.service;


import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.post.Post;
import org.example.demo3.domain.post.dto.PostRequestDto;
import org.example.demo3.domain.post.dto.PostResponseDto;
import org.example.demo3.domain.post.dto.PostUpdateDto;
import org.example.demo3.domain.post.repository.PostRepository;
import org.example.demo3.domain.user.User;
import org.example.demo3.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //DTO -> 엔티티 변환 + 연관관계 주입을 서비스 내부에서 책임지게 개선
    //1. 글 작성
    public void save(PostRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();

        postRepository.save(post);
    }


    // 2. 전체 글 조회
    public Page<PostResponseDto> findAllPaged(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostResponseDto::new);
    }

    // 3. 글 단건 조회
    public PostResponseDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
        return new PostResponseDto(post);
    }

    // 4. 글 수정
    @Transactional
    public void update(Long id, PostUpdateDto dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));
        post.update(dto.getTitle(), dto.getContent());
    }

    // 5. 글 삭제
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}

