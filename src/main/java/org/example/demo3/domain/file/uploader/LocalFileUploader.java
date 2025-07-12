package org.example.demo3.domain.file.uploader;

import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.file.File;
import org.example.demo3.global.util.ImageProcessUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component("localFileUploader")
@RequiredArgsConstructor
public class LocalFileUploader implements FileUploader {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${uploader.local-base-url}")
    private String localBaseUrl;

    @Override
    public File storeFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();

        byte[] fileBytes;
        String storedFilename;
        String contentType;

        if (ImageProcessUtil.isImage(multipartFile)) {
            BufferedImage resizedImage = ImageProcessUtil.resizeToBufferedImage(multipartFile, 1080);
            try {
                fileBytes = ImageProcessUtil.convertToWebPUsingCLI(resizedImage, 0.8f);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("WebP 변환 중 인터럽트 발생", e);
            }
            storedFilename = uuid + ".webp";
            contentType = "image/webp";
        } else {
            fileBytes = multipartFile.getBytes();
            String extension = ImageProcessUtil.getExtension(originalFilename).orElse("bin");
            storedFilename = uuid + "." + extension;
            contentType = multipartFile.getContentType();
        }

        String fullPath = uploadDir + storedFilename;

        // 파일 저장
        try (FileOutputStream fos = new FileOutputStream(fullPath)) {
            fos.write(fileBytes);
        }

        return File.builder()
                .originalFilename(originalFilename)
                .storedFilename(storedFilename)
                .filePath(fullPath)
                .size((long) fileBytes.length)
                .contentType(contentType)
                .uploadedAt(LocalDateTime.now())
                .url(localBaseUrl + "/uploads/" + storedFilename)
                .build();
    }
}



