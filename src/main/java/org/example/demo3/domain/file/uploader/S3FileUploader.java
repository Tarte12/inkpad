package org.example.demo3.domain.file.uploader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.file.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Component("s3FileUploader")
public class S3FileUploader implements FileUploader {

    private final AmazonS3 amazonS3;

    // ✅ @Value로 직접 주입
    @Value("${cloud.aws.s3.bucket}")
    private String s3BucketName;

    @Override
    public File storeFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String storedFilename = uuid + ext;
        String s3Key = "uploads/" + storedFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        amazonS3.putObject(s3BucketName.trim(), s3Key, multipartFile.getInputStream(), metadata);

        return File.builder()
                .originalFilename(originalFilename)
                .storedFilename(storedFilename)
                .filePath(s3Key)
                .size(multipartFile.getSize())
                .contentType(multipartFile.getContentType())
                .uploadedAt(LocalDateTime.now())
                .build();
    }
}



