package org.example.demo3.domain.file.uploader;

import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.file.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component("localFileUploader")
@RequiredArgsConstructor
public class LocalFileUploader implements FileUploader {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public File storeFile(MultipartFile multipartFile) throws IOException {
        // 1. original filename
        String originalFilename = multipartFile.getOriginalFilename();

        // 2. UUID + 확장자
        String uuid = UUID.randomUUID().toString();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String storedFilename = uuid + ext;

        // 3. 실제 저장 경로
        String fullPath = uploadDir + storedFilename;  // ← 경로도 수정
        multipartFile.transferTo(new java.io.File(fullPath));

        // 4. File 엔티티 반환
        return File.builder()
                .originalFilename(originalFilename)
                .storedFilename(storedFilename)
                .filePath(fullPath)
                .size(multipartFile.getSize())
                .contentType(multipartFile.getContentType())
                .uploadedAt(LocalDateTime.now())
                .build();
    }
}

