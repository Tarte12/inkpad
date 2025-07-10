package org.example.demo3.domain.file.uploader;

import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.file.File;
import org.example.demo3.global.util.ImageResizeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
        ImageIO.scanForPlugins();
        String originalFilename = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String storedFilename = uuid + ".webp";
        String fullPath = uploadDir + storedFilename;

        byte[] bytes;
        String contentType;

        if (multipartFile.getContentType() != null && multipartFile.getContentType().startsWith("image")) {
            BufferedImage resizedImage = ImageResizeUtil.resize(multipartFile, 1080);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            boolean written = ImageIO.write(resizedImage, "webp", os);
            if (!written) {
                throw new IOException("WebP 변환 실패: ImageIO.write 실패");
            }
            bytes = os.toByteArray();
            contentType = "image/webp";
        } else {
            bytes = multipartFile.getBytes();
            contentType = multipartFile.getContentType();
        }

        java.io.File outputFile = new java.io.File(fullPath);
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(outputFile)) {
            fos.write(bytes);
        }

        return File.builder()
                .originalFilename(originalFilename)
                .storedFilename(storedFilename)
                .filePath(fullPath)
                .size((long) bytes.length)
                .contentType(contentType)
                .uploadedAt(LocalDateTime.now())
                .url(localBaseUrl + "/uploads/" + storedFilename)  // ✅ CDN 또는 프론트에서 접근 가능한 URL
                .build();
    }
}

