package org.example.demo3.domain.file.repository;

import org.example.demo3.domain.file.File;
import org.example.demo3.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

    void deleteByPost(Post post);
}
