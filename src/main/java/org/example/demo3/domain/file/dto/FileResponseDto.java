package org.example.demo3.domain.file.dto;

import org.example.demo3.domain.file.File;

public class FileResponseDto {
    private final String originalFilename;
    private final String storedFilename;  //필요하면 프론트에서 다운로드 구분용
    private final String url;

    public FileResponseDto(File file) {
        this.originalFilename = file.getOriginalFilename();
        this.storedFilename = file.getStoredFilename();  // ✅ 추가 (선택사항)
        this.url = "/uploads/" + file.getStoredFilename();
    }
}
