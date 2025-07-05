package org.example.demo3.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PopularPostDto {
    private Long id;
    private String title;
    private int views;
}
