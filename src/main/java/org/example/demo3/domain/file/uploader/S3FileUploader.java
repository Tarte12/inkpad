package org.example.demo3.domain.file.uploader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.file.File;
import org.example.demo3.domain.file.FileType;
import org.example.demo3.global.util.FileTypeUtil;
import org.example.demo3.global.util.ImageProcessUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        FileType fileType;

        // ✅ 이미지 처리
        if (FileTypeUtil.isImage(multipartFile)) {
            BufferedImage resizedImage = ImageProcessUtil.resizeToBufferedImage(multipartFile, 400);
            try {
                fileBytes = ImageProcessUtil.convertToWebPUsingCLI(resizedImage, 0.8f);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("WebP 변환 중 인터럽트 발생", e);
            }
            storedFilename = uuid + ".webp";
            contentType = "image/webp";
            fileType = FileType.IMAGE;
        }

        // ✅ 엑셀 처리
        else if (FileTypeUtil.isExcel(multipartFile)) {
            fileBytes = multipartFile.getBytes();
            storedFilename = uuid + ".xlsx";
            contentType = FileTypeUtil.getContentType(multipartFile);
            fileType = FileType.EXCEL;
        }

        // ✅ PDF 처리
        else if (FileTypeUtil.isPdf(multipartFile)) {
            fileBytes = multipartFile.getBytes();
            storedFilename = uuid + ".pdf";
            contentType = FileTypeUtil.getContentType(multipartFile);
            fileType = FileType.PDF;
        }

        // ✅ 그 외 일반 파일
        else {
            fileBytes = multipartFile.getBytes();
            String ext = FileTypeUtil.getExtension(originalFilename).orElse("bin");
            storedFilename = uuid + "." + ext;
            contentType = FileTypeUtil.getContentType(multipartFile);
            fileType = FileType.OTHER;
        }

        // ✅ S3 업로드용 key 설정
        String s3Key = "uploads/" + storedFilename;

        // ✅ 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileBytes.length);
        metadata.setContentType(contentType);
        metadata.setContentDisposition("inline; filename*=UTF-8''" +
                URLEncoder.encode(originalFilename, StandardCharsets.UTF_8));

        // ✅ S3 업로드
        amazonS3.putObject(s3BucketName.trim(), s3Key, new ByteArrayInputStream(fileBytes), metadata);

        // ✅ File 객체 반환
        return File.builder()
                .originalFilename(originalFilename)
                .storedFilename(storedFilename)
                .filePath(s3Key)
                .size((long) fileBytes.length)
                .contentType(contentType)
                .uploadedAt(LocalDateTime.now())
                .url(cloudFrontUrl + "/" + s3Key)
                .fileType(fileType)
                .build();
    }

    // S3FileUploader.java 안에 추가해 줘야 하는 setter
    public void setS3BucketName(String s3BucketName) {
        this.s3BucketName = s3BucketName;
    }

    public void setCloudFrontUrl(String cloudFrontUrl) {
        this.cloudFrontUrl = cloudFrontUrl;
    }

}






