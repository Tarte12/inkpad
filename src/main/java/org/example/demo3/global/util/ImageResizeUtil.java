package org.example.demo3.global.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageResizeUtil {

    public static BufferedImage resize(MultipartFile multipartFile, int targetWidth) throws IOException {
        BufferedImage originalImage = javax.imageio.ImageIO.read(multipartFile.getInputStream());

        // ✅ 이미지가 아닌 경우 처리
        if (originalImage == null) {
            throw new IllegalArgumentException("업로드된 파일은 이미지가 아닙니다.");
        }

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        int targetHeight = (int) ((double) originalHeight / originalWidth * targetWidth);

        System.out.println("📌 리사이징 전: " + originalWidth + "x" + originalHeight);
        System.out.println("📌 리사이징 후: " + targetWidth + "x" + targetHeight);

        return Thumbnails.of(originalImage)
                .size(targetWidth, targetHeight)
                .asBufferedImage();
    }
}

