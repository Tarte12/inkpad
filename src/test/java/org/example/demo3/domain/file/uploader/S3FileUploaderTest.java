package org.example.demo3.domain.file.uploader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.example.demo3.domain.file.File;
import org.example.demo3.global.util.ImageProcessUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class S3FileUploaderTest {

    private AmazonS3 amazonS3;
    private S3FileUploader s3FileUploader;

    @BeforeEach
    void setUp() {
        amazonS3 = mock(AmazonS3.class);

        s3FileUploader = new S3FileUploader(amazonS3);
        // 필드 수동 주입
        s3FileUploader.setS3BucketName("my-test-bucket");
        s3FileUploader.setCloudFrontUrl("https://cdn.example.com");
    }

    @Test
    void 이미지파일_업로드_성공_WebP() throws Exception {
        // given
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        byte[] webpBytes = ImageProcessUtil.convertToWebPUsingCLI(image, 0.8f);

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                webpBytes
        );

        // when
        File result = s3FileUploader.storeFile(multipartFile);

        // then
        assertThat(result.getOriginalFilename()).isEqualTo("test.jpg");
        assertThat(result.getStoredFilename()).endsWith(".webp");
        assertThat(result.getFilePath()).contains("uploads/");
        assertThat(result.getUrl()).startsWith("https://cdn.example.com");
        assertThat(result.getContentType()).isEqualTo("image/webp");
        assertThat(result.getSize()).isEqualTo(webpBytes.length);

        // verify S3 업로드 호출 여부
        ArgumentCaptor<ByteArrayInputStream> inputCaptor = ArgumentCaptor.forClass(ByteArrayInputStream.class);
        verify(amazonS3).putObject(
                eq("my-test-bucket"),
                contains("uploads/"),
                inputCaptor.capture(),
                any(ObjectMetadata.class)
        );
    }
}