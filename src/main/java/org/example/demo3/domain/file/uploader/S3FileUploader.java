package org.example.demo3.domain.file.uploader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.file.File;
import org.example.demo3.global.util.ImageProcessUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component("s3FileUploader")
@Primary
@ConditionalOnProperty(name = "uploader.s3.enabled", havingValue = "true")
@RequiredArgsConstructor
public class S3FileUploader implements FileUploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String s3BucketName;

    @Value("${cloudfront.url}")
    private String cloudFrontUrl;

    @Override
    public File storeFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();

        byte[] fileBytes;
        String storedFilename;
        String contentType;

        if (ImageProcessUtil.isImage(multipartFile)) {
            BufferedImage resizedImage = ImageProcessUtil.resizeToBufferedImage(multipartFile, 400);
            try {
                fileBytes = ImageProcessUtil.convertToWebPUsingCLI(resizedImage, 0.8f); // CLI 기반 WebP 변환
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("WebP 변환 중 인터럽트 발생", e);
            }
            storedFilename = uuid + ".webp";
            contentType = "image/webp";
        } else {
            fileBytes = multipartFile.getBytes();
            storedFilename = uuid + "." + ImageProcessUtil.getExtension(originalFilename).orElse("bin");
            contentType = multipartFile.getContentType();
        }

        String s3Key = "uploads/" + storedFilename;

        // S3 업로드
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileBytes.length);
        metadata.setContentType(contentType);

        amazonS3.putObject(s3BucketName.trim(), s3Key, new ByteArrayInputStream(fileBytes), metadata);

        return File.builder()
                .originalFilename(originalFilename)
                .storedFilename(storedFilename)
                .filePath(s3Key)
                .size((long) fileBytes.length)
                .contentType(contentType)
                .uploadedAt(LocalDateTime.now())
                .url(cloudFrontUrl + "/" + s3Key)
                .build();
    }
}





