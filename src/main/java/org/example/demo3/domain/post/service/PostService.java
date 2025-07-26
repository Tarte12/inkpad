package org.example.demo3.domain.post.service;


import org.example.demo3.domain.file.File;
import org.example.demo3.domain.file.repository.FileRepository;
import org.example.demo3.domain.file.service.FileService;
import org.example.demo3.domain.post.Post;
import org.example.demo3.domain.post.dto.*;
import org.example.demo3.domain.post.repository.PostRepository;
import org.example.demo3.domain.user.User;
import org.example.demo3.domain.user.UserRole;
import org.example.demo3.domain.user.repository.UserRepository;
import org.example.demo3.global.exception.BlogException;
import org.example.demo3.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //File 기능 추가를 위해
    private final FileRepository fileRepository;
    private final FileService fileService;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       FileRepository fileRepository,
                       FileService fileService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
    }

    //DTO -> 엔티티 변환 + 연관관계 주입을 서비스 내부에서 책임지게 개선
    //1. 글 작성
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Transactional
    public void createPost(Long userId, PostRequestDto dto, List<MultipartFile> files) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BlogException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile multipartFile : files) {
                File file = fileService.storeFile(multipartFile); //유효성 검사 + 저장
                post.addFile(file);
            }
        }

        postRepository.save(post);
    }


    // 2. 전체 글 조회
    public Page<PostResponseDto> findAllPaged(Pageable pageable) {
        return postRepository.findAllWithUserPaged(pageable)
                .map(PostResponseDto::new);
    }

    // 3. 글 단건 조회
    public PostResponseDto findById(Long id) {
        Post post = postRepository.findWithUserById(id)
                .orElseThrow(() -> new BlogException(ErrorCode.POST_NOT_FOUND));
        return new PostResponseDto(post);
    }


    // 4. 글 수정
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Transactional
    public void update(Long postId, Long userId, PostUpdateDto dto, List<MultipartFile> files) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BlogException(ErrorCode.USER_NOT_FOUND));

// 수정 시에도 fetch join된 메서드 사용
        Post post = postRepository.findWithUserById(postId)
                .orElseThrow(() -> new BlogException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId()) && user.getRole() != UserRole.ADMIN) {
            throw new BlogException(ErrorCode.UNAUTHORIZED); // 👈 본인/관리자만 허용
        }

        post.update(dto.getTitle(), dto.getContent());

        // 기존 파일 제거
        post.clearFiles(); // 연관관계 해제
        fileRepository.deleteByPost(post); // DB에서도 제거

        // 새 파일 추가
        if (files != null && !files.isEmpty()) {
            for (MultipartFile multipartFile : files) {
                File file = fileService.storeFile(multipartFile);
                post.addFile(file);
            }
        }
    }



    // 5. 글 삭제
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void delete(Long postId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BlogException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findWithUserById(postId)
                .orElseThrow(() -> new BlogException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId()) && user.getRole() != UserRole.ADMIN) {
            throw new BlogException(ErrorCode.UNAUTHORIZED);
        }

        postRepository.deleteById(postId);
    }

    public List<CategoryCountDto> getPostCountByCategory() {
        return postRepository.countPostsByCategory();
    }

    public List<PopularPostDto> getPopularPosts() {
        return postRepository.findPopularPosts();
    }

}

