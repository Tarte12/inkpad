package org.example.demo3.domain.file;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String originalFilename; //업로드된 이름
    private String storedFilename; //UUID 등으로 변환된 이름
    private String filePath; //파일 저장 위치 경로
    private Long size; //크기
    private String contentType; //MIME 타입
    private LocalDateTime uploadedAt; //업로드 일시


    public File(String originalFilename, String storedFilename, String filePath, Long size, String contentType,LocalDateTime uploadedAt){
        this.id = id;

        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.filePath = filePath;
        this.size = size;
        this.contentType = contentType;
        this.uploadedAt = uploadedAt;
    }

    //파일 이름만 변경하는 메서드(사용자 직접 조작)
    //엔티티 내부의 데이터만 바꾸는 메서드
    //TV 리모컨 => 사용자가 리모컨을 찾아 TV를 켜는 행위는 FileService에 만들어야 함
    public void updateFilename(String newFilename){
        this.originalFilename = newFilename;

    }
    //주의: 파일 교체는 '삭제 -> 새로 생성' 방식으로 처리

}
