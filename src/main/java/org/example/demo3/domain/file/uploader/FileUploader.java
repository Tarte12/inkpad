package org.example.demo3.domain.file.uploader;

import org.example.demo3.domain.file.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploader {
    File storeFile(MultipartFile multipartFile) throws IOException;
}
