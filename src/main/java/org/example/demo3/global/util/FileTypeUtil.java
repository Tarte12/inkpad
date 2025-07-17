package org.example.demo3.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public class FileTypeUtil {

    /**
     * ✅ 이미지 파일 여부 판별
     */
    public static boolean isImage(MultipartFile file) {
        String type = file.getContentType();
        return type != null && type.startsWith("image");
    }

    /**
     * ✅ 엑셀 파일 여부 판별
     */
    public static boolean isExcel(MultipartFile file) {
        String type = file.getContentType();
        return type != null && type.contains("spreadsheet");
    }

    /**
     * ✅ PDF 파일 여부 판별
     */
    public static boolean isPdf(MultipartFile file) {
        String type = file.getContentType();
        return "application/pdf".equals(type);
    }

    /**
     * ✅ 그 외 일반 파일
     */
    public static boolean isOther(MultipartFile file) {
        return !isImage(file) && !isExcel(file) && !isPdf(file);
    }

    /**
     * ✅ 확장자 추출
     */
    public static Optional<String> getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return Optional.empty();
        return Optional.of(filename.substring(filename.lastIndexOf('.') + 1).toLowerCase());
    }

    /**
     * ✅ Content-Type 기본값 설정 (null 방지)
     */
    public static String getSafeContentType(MultipartFile file) {
        return file.getContentType() != null ? file.getContentType() : "application/octet-stream";
    }

    public static String getContentType(MultipartFile file) {
        if (file == null || file.getContentType() == null) {
            return "application/octet-stream";
        }
        return file.getContentType();
    }
}
