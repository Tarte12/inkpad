package org.example.demo3.global.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;

public class ImageProcessUtil {

    /**
     * MultipartFile에서 BufferedImage를 읽어옵니다.
     */
    public static BufferedImage readImageFromMultipartFile(MultipartFile multipartFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(multipartFile.getInputStream());
        if (originalImage == null) {
            throw new IllegalArgumentException("업로드된 파일은 이미지가 아니거나 지원되지 않는 형식입니다.");
        }
        return originalImage;
    }

    /**
     * BufferedImage를 리사이징합니다 (가로 기준).
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth) throws IOException {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        if (originalWidth <= targetWidth) {
            return originalImage; // 업스케일링 방지
        }

        int targetHeight = (int) ((double) originalHeight / originalWidth * targetWidth);

        return Thumbnails.of(originalImage)
                .size(targetWidth, targetHeight)
                .asBufferedImage();
    }

    /**
     * MultipartFile → 리사이즈된 BufferedImage
     */
    public static BufferedImage resizeToBufferedImage(MultipartFile multipartFile, int targetWidth) throws IOException {
        BufferedImage originalImage = readImageFromMultipartFile(multipartFile);
        return resizeImage(originalImage, targetWidth);
    }

    /**
     * BufferedImage → WebP 바이트 변환 (cwebp CLI 사용)
     */
    public static byte[] convertToWebPUsingCLI(BufferedImage image, float quality) throws IOException, InterruptedException {
        // 1. PNG 임시 저장
        File tempPng = File.createTempFile("temp-", ".png");
        ImageIO.write(image, "png", tempPng);

        // 2. WebP 결과 파일 경로
        File tempWebP = File.createTempFile("temp-", ".webp");

        // 3. cwebp 실행
        ProcessBuilder pb = new ProcessBuilder(
                "cwebp",
                "-q", String.valueOf((int)(quality * 100)), // 0~100
                tempPng.getAbsolutePath(),
                "-o", tempWebP.getAbsolutePath()
        );

        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0 || !tempWebP.exists()) {
            tempPng.delete();
            tempWebP.delete();
            throw new RuntimeException("WebP 변환 실패 (exitCode = " + exitCode + ")");
        }

        byte[] result = Files.readAllBytes(tempWebP.toPath());

        // 정리
        tempPng.delete();
        tempWebP.delete();

        return result;
    }

    /**
     * BufferedImage를 byte[]로 변환합니다 (테스트용 또는 일반 저장용).
     */
    public static byte[] bufferedImageToBytes(BufferedImage image, String formatName) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, formatName, baos);
            return baos.toByteArray();
        }
    }
}

