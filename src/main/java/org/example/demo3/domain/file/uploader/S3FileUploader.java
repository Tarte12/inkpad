package org.example.demo3.domain.file.uploader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.file.File;
import org.example.demo3.global.util.ImageResizeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    @Value("${cloudfront-url}")
    private String cloudFrontUrl;

    @Override
    public File storeFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf('.') + 1)
                : "jpg"; // fallback

        String uuid = UUID.randomUUID().toString();
        String storedFilename = uuid + "." + extension; // ✅ 원본 확장자 유지
        String s3Key = "uploads/" + storedFilename;

        byte[] bytes;
        String contentType;

        // ✅ 이미지라면 리사이징만 적용
        if (multipartFile.getContentType() != null && multipartFile.getContentType().startsWith("image")) {
            BufferedImage resizedImage = ImageResizeUtil.resize(multipartFile, 1080);
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            // ✅ 원본 포맷으로 저장
            ImageIO.write(resizedImage, extension, os);

            bytes = os.toByteArray();
            contentType = multipartFile.getContentType();
        } else {
            // 일반 파일
            bytes = multipartFile.getBytes();
            contentType = multipartFile.getContentType();
        }

        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        metadata.setContentType(contentType);

        amazonS3.putObject(s3BucketName.trim(), s3Key, is, metadata);

        return File.builder()
                .originalFilename(originalFilename)
                .storedFilename(storedFilename)
                .filePath(s3Key)
                .size((long) bytes.length)
                .contentType(contentType)
                .uploadedAt(LocalDateTime.now())
                .url(cloudFrontUrl + "/" +s3Key)
                .build();
    }

}



