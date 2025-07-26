package org.example.demo3.domain.file.uploader;

import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.file.File;
import org.example.demo3.domain.file.FileType;
import org.example.demo3.global.util.FileTypeUtil;
import org.example.demo3.global.util.ImageProcessUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
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
        FileType fileType;

        // ✅ 이미지 처리: 리사이즈 + WebP 변환
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

        // ✅ 엑셀 파일 처리
        else if (FileTypeUtil.isExcel(multipartFile)) {
            fileBytes = multipartFile.getBytes();
            storedFilename = uuid + ".xlsx";
            contentType = FileTypeUtil.getContentType(multipartFile);
            fileType = FileType.EXCEL;
        }

        // ✅ PDF 파일 처리
        else if (FileTypeUtil.isPdf(multipartFile)) {
            fileBytes = multipartFile.getBytes();
            storedFilename = uuid + ".pdf";
            contentType = FileTypeUtil.getContentType(multipartFile);
            fileType = FileType.PDF;
        }

        // ✅ 그 외 파일 처리
        else {
            fileBytes = multipartFile.getBytes();
            String ext = FileTypeUtil.getExtension(originalFilename).orElse("bin");
            storedFilename = uuid + "." + ext;
            contentType = FileTypeUtil.getContentType(multipartFile);
            fileType = FileType.OTHER;
        }

        // ✅ 로컬 파일 저장
        String filePath = Paths.get(uploadDir, storedFilename).toString();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileBytes);
        }

        return File.builder()
                .originalFilename(originalFilename)
                .storedFilename(storedFilename)
                .filePath(filePath)
                .size((long) fileBytes.length)
                .contentType(contentType)
                .uploadedAt(LocalDateTime.now())
                .url(localBaseUrl + "/" + storedFilename)
                .fileType(fileType)
                .build();
    }

    // 테스트용 setter
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public void setLocalBaseUrl(String localBaseUrl) {
        this.localBaseUrl = localBaseUrl;
    }

}




