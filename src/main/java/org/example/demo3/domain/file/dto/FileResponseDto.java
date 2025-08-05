package org.example.demo3.domain.file.dto;

import org.example.demo3.domain.file.File;

public class FileResponseDto {
    private final String originalFilename;
    private final String storedFilename;
    private final String url;  // ✅ S3/CloudFront or Local/Cloudflare 등 실제 접근 경로

    public FileResponseDto(File file) {
        this.originalFilename = file.getOriginalFilename();
        this.storedFilename = file.getStoredFilename();
        this.url = file.getUrl(); // ✅ 실제 접근 URL로 교체
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public String getUrl() {
        return url;
    }
}
