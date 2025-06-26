package org.example.demo3.domain.post.controller;


import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.post.Post;
import org.example.demo3.domain.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        return ResponseEntity.ok(postService.create(post));
    }

    @GetMapping
    public ResponseEntity<List<Post>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findById(@PathVariable Long id) {
        return postService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Post post) {
        postService.update(id, post.getTitle(), post.getContent());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

