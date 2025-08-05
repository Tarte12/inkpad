package org.example.demo3.domain.file.service;

import lombok.RequiredArgsConstructor;
import org.example.demo3.domain.file.File;
import org.example.demo3.domain.file.uploader.FileUploader;
import org.example.demo3.global.exception.BlogException;
import org.example.demo3.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".webp");

    private final FileUploader fileUploader;

    public FileService(FileUploader fileUploader) {
        this.fileUploader = fileUploader;
    }

    public File storeFile(MultipartFile multipartFile) throws IOException {
        validateFile(multipartFile);
        return fileUploader.storeFile(multipartFile);
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BlogException(ErrorCode.FILE_SIZE_EXCEEDED);
        }

        String filename = file.getOriginalFilename();
        if (filename == null || ALLOWED_EXTENSIONS.stream().noneMatch(filename.toLowerCase()::endsWith)) {
            throw new BlogException(ErrorCode.UNSUPPORTED_FILE_TYPE);
        }
    }
}

