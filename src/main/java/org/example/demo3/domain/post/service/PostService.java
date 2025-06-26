package org.example.demo3.domain.post.service;


import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.post.Post;
import org.example.demo3.domain.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post create(Post post) {
        return postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public void update(Long id, String title, String content) {
        Post post = postRepository.findById(id).orElseThrow();
        post.update(title, content);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}

