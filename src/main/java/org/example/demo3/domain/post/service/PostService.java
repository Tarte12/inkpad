package org.example.demo3.domain.post.service;


import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.file.File;
import org.example.demo3.domain.file.repository.FileRepository;
import org.example.demo3.domain.file.uploader.FileUploader;
import org.example.demo3.domain.post.Post;
import org.example.demo3.domain.post.dto.*;
import org.example.demo3.domain.post.repository.PostRepository;
import org.example.demo3.domain.user.User;
import org.example.demo3.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //File 기능 추가를 위해
    private final FileUploader fileUploader;
    private final FileRepository fileRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       FileRepository fileRepository,
                       @Qualifier("s3FileUploader") FileUploader fileUploader) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.fileUploader = fileUploader;
    }

    //DTO -> 엔티티 변환 + 연관관계 주입을 서비스 내부에서 책임지게 개선
    //1. 글 작성
    @Transactional
    public void createPost(PostRequestDto dto, List<MultipartFile> files) throws Exception {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile multipartFile : files) {
                File file = fileUploader.storeFile(multipartFile); // ✅ 파일 저장
                post.addFile(file); // ✅ Post와 연관관계 설정
            }
        }

        postRepository.save(post); // ✅ cascade 로 파일까지 저장
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
    public void update(Long id, PostUpdateDto dto, List<MultipartFile> files) throws Exception {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        post.update(dto.getTitle(), dto.getContent());

        // 기존 파일 제거
        post.clearFiles(); // 연관관계 해제
        fileRepository.deleteByPost(post); // DB에서도 제거

        // 새 파일 추가
        if (files != null && !files.isEmpty()) {
            for (MultipartFile multipartFile : files) {
                File file = fileUploader.storeFile(multipartFile);
                post.addFile(file);
            }
        }
    }



    // 5. 글 삭제
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public List<CategoryCountDto> getPostCountByCategory() {
        return postRepository.countPostsByCategory();
    }

    public List<PopularPostDto> getPopularPosts() {
        return postRepository.findPopularPosts();
    }

}

