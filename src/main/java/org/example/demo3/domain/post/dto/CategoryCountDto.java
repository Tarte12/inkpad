package org.example.demo3.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//통계용 DTO
@Getter
@NoArgsConstructor
@AllArgsConstructor 
//JPQL에서는 파라미터 있는 생성자가 필요한데 이 어노테이션이 lombok이 생성자 만들게 해 줌
public class CategoryCountDto {
    private  String category;
    private Long count;
}
